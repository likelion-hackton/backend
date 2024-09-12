package com.example.backend.member.service;

import com.example.backend.common.jwt.JwtTokenUtil;
import com.example.backend.common.jwt.refresh.entity.RefreshToken;
import com.example.backend.common.jwt.refresh.converter.RefreshTokenConverter;
import com.example.backend.common.jwt.refresh.repository.RefreshTokenRepository;
import com.example.backend.email.EmailSenderService;
import com.example.backend.email.EmailVerifyService;
import com.example.backend.member.entity.Member;
import com.example.backend.member.converter.MemberConverter;
import com.example.backend.member.entity.dto.request.*;
import com.example.backend.member.entity.dto.response.EmailVerifyResponseDTO;
import com.example.backend.member.entity.dto.response.JwtTokenResponseDTO;
import com.example.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

@Service
@Primary
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailSenderService emailSenderService;
    private final EmailVerifyService emailVerifyService;
    private final PasswordEncoder passwordEncoder;

    // 성능면에서 좋은 로그로 처리
    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Value("${spring.jwt.expirationTime}") // Access Token 만료시간
    private Long expirationTime;
    @Value("${spring.jwt.refresh-expirationTime}") // Refresh Token 만료시간
    private Long refreshExpirationTime;
    @Value("${spring.jwt.secretKey}") // 토큰용 비밀키
    private String secretKey;

    public void verifyCode(VerifyCodeRequestDTO req) {
        if(!emailVerifyService.verifyCode(req.getEmail(), req.getVerification())){
            logger.warn("유효하지 않은 인증코드");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 인증코드");
        }
    }

    // 회원가입
    @Transactional
    public void signupMember(SignupRequestDTO req){
        if (checkEmailDuplication(req.getEmail())){
            logger.warn("이메일 중복");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 중복");
        }
        if (!req.getPassword().equals(req.getCheckPassword())){
            logger.warn("비밀번호 일치하지 않음");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호 일치하지 않음");
        }
        // 패스워드 인코딩 후 저장
        req.setPassword(passwordEncoder.encode(req.getPassword()));
        memberRepository.save(MemberConverter.signupReqeustConverter(req));
    }

    // 이메일 중복 확인
    public boolean checkEmailDuplication(String email){
        return memberRepository.existsByEmail(email);
    }

    // 인증메일 전송
    public EmailVerifyResponseDTO sendVerificationEmail(EmailVerifyRequestDTO req){
        // 이메일 전송
        emailSenderService.sendVerification(req.getEmail(), emailVerifyService.generateVerificationCode(req.getEmail()));
        return MemberConverter.emailVerifyResponseConverter(req);
    }

    // 로그인
    @Transactional
    public JwtTokenResponseDTO login(LoginRequestDTO req){
        // 사용자 존재 여부 확인
        Member member = memberRepository.findByEmail(req.getEmail()).orElse(null);
        if(member == null){
            logger.warn("사용자 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 찾을 수 없음");
        }
        if(!passwordEncoder.matches(req.getPassword(), member.getPassword())){
            logger.warn("비밀번호가 일치하지 않음");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않음");
        }

        // 토큰 반환
        return MemberConverter.jwtTokenResponseConverter(
                JwtTokenUtil.createToken(member.getEmail(), secretKey, expirationTime),
                expirationTime.toString(),
                createRefreshToken(member).getRefreshToken(),
                refreshExpirationTime.toString()
        );
    }

    // Refresh Token 생성
    @Transactional
    public RefreshToken createRefreshToken(Member member){

        // DB에 저장하기 적합한 Instant 형으로 만료시간 저장
        Instant instant = Instant.now().plusMillis(refreshExpirationTime);

        // 토큰 생성 저장 후 전송
        String refreshToken = JwtTokenUtil.createRefreshToken(member.getEmail(), secretKey, refreshExpirationTime);

        // 멤버가 이미 토큰을 보유했는지
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByMember(member);

        if(existingToken.isPresent()){
            RefreshToken token = existingToken.get();
            token.setRefreshToken(refreshToken);
            token.setRefreshExpiresTime(instant);
            return refreshTokenRepository.save(token);
        }
        return refreshTokenRepository.save(RefreshTokenConverter.createTokenConverter(refreshToken, member, instant));
    }

    // Refresh Token 유효 확인
    @Transactional
    public JwtTokenResponseDTO verifyRefreshToken(RefreshRequestDTO req){
        // 토큰 존재 확인
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(req.getRefresh_token()).orElse(null);
        if(refreshToken == null){
            logger.warn("존재하지 않는 토큰");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 토큰");
        }
        // 만료시간 확인
        if (refreshToken.getRefreshExpiresTime().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(refreshToken);
            logger.warn("유효하지 않는 토큰");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않는 토큰");
        }

        // 토큰에서 이메일 추출
        String email = JwtTokenUtil.getEmail(req.getRefresh_token(), secretKey);

        // Access Token 다시 발급 후 전송
        return MemberConverter.jwtTokenResponseConverter(
                JwtTokenUtil.createToken(email, secretKey, expirationTime),
                expirationTime.toString(),
                req.getRefresh_token(),
                refreshExpirationTime.toString()
        );
    }

    // 로그아웃
    @Transactional
    public void logout(String email){
        // 멤버 존재 확인
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member == null){
            logger.warn("사용자 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 찾을 수 없음");
        }
        // Refresh Token 삭제
        refreshTokenRepository.deleteByMember(member);
    }

    // Id로 멤버 찾기
    public Member getMemberById(Long id){
        Optional<Member> findMember = memberRepository.findById(id);
        return findMember.orElse(null);
    }

    // 이메일로 멤버 찾기
    public Member getMemberByEmail(String email){
        Optional<Member> findMember = memberRepository.findByEmail(email);
        return findMember.orElse(null);
    }
}
