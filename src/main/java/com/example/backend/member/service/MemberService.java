package com.example.backend.member.service;

import com.example.backend.common.jwt.JwtTokenUtil;
import com.example.backend.common.jwt.refresh.entity.RefreshToken;
import com.example.backend.common.jwt.refresh.entity.RefreshTokenConverter;
import com.example.backend.common.jwt.refresh.repository.RefreshTokenRepository;
import com.example.backend.email.EmailSenderService;
import com.example.backend.email.EmailVerifyService;
import com.example.backend.member.entity.Member;
import com.example.backend.member.entity.dto.MemberDtoConverter;
import com.example.backend.member.entity.dto.request.EmailVerifyRequestDTO;
import com.example.backend.member.entity.dto.request.LoginRequestDTO;
import com.example.backend.member.entity.dto.request.RefreshRequestDTO;
import com.example.backend.member.entity.dto.request.SignupRequestDTO;
import com.example.backend.member.entity.dto.response.EmailVerifyResponseDTO;
import com.example.backend.member.entity.dto.response.JwtTokenResponseDTO;
import com.example.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
@Primary
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailSenderService emailSenderService;
    private final EmailVerifyService emailVerifyService;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.jwt.expirationTime}")
    private Long expirationTime;
    @Value("${spring.jwt.refresh-expirationTime}")
    private Long refreshExpirationTime;
    @Value("${spring.jwt.secretKey}")
    private String secretKey;

    public void signupMember(SignupRequestDTO req){
        if(!emailVerifyService.verifyCode(req.getEmail(), req.getVerification())){
            System.out.println("유효하지 않은 인증코드");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 인증코드");
        }
        if (checkEmailDuplication(req.getEmail())){
            System.out.println("이메일 중복");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 중복");
        }
        if (!req.getPassword().equals(req.getCheckPassword())){
            System.out.println("비밀번호 일치하지 않음");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호 일치하지 않음");
        }
        req.setPassword(passwordEncoder.encode(req.getPassword()));
        memberRepository.save(MemberDtoConverter.signupReqeustConverter(req));
    }

    public boolean checkEmailDuplication(String email){
        return memberRepository.existsByEmail(email);
    }

    public EmailVerifyResponseDTO sendVerificationEmail(EmailVerifyRequestDTO req){
        emailSenderService.sendVerification(req.getEmail(), emailVerifyService.generateVerificationCode(req.getEmail()));
        return MemberDtoConverter.emailVerifyResponseConverter(req);
    }

    public JwtTokenResponseDTO login(LoginRequestDTO req){
        Member member = memberRepository.findByEmail(req.getEmail()).orElse(null);
        if(member == null){
            System.out.println("존재하지 않는 사용자");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자");
        }
        if(!passwordEncoder.matches(req.getPassword(), member.getPassword())){
            System.out.println("비밀번호가 일치하지 않음");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않음");
        }

        return MemberDtoConverter.jwtTokenResponseConverter(
                JwtTokenUtil.createToken(req.getEmail(), secretKey, expirationTime),
                expirationTime.toString(),
                createRefreshToken(req.getEmail()).getRefreshToken(),
                refreshExpirationTime.toString()
        );
    }

    public RefreshToken createRefreshToken(String email){
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member == null){
            System.out.println("존재하지 않는 사용자");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자");
        }

        // DB에 저장하기 적합한 Instant 형으로 만료시간 저장
        Instant instant = Instant.now().plusMillis(refreshExpirationTime);
        String refreshToken = JwtTokenUtil.createRefreshToken(email, secretKey, refreshExpirationTime);
        return refreshTokenRepository.save(RefreshTokenConverter.createTokenConverter(refreshToken, member, instant));
    }

    public JwtTokenResponseDTO verifyRefreshToken(RefreshRequestDTO req){
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(req.getRefresh_token()).orElse(null);
        if(refreshToken == null){
            System.out.println("존재하지 않는 토큰");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 토큰");
        }
        if (refreshToken.getRefreshExpiresTime().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(refreshToken);
            System.out.println("유효하지 않는 토큰");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않는 토큰");
        }

        String email = JwtTokenUtil.getEmail(req.getRefresh_token(), secretKey);

        return MemberDtoConverter.jwtTokenResponseConverter(
                JwtTokenUtil.createToken(email, secretKey, expirationTime),
                expirationTime.toString(),
                req.getRefresh_token(),
                refreshExpirationTime.toString()
        );
    }

    public Member getMemberById(Long id){
        Optional<Member> findMember = memberRepository.findById(id);
        return findMember.orElse(null);
    }

    public Member getMemberByEmail(String email){
        Optional<Member> findMember = memberRepository.findByEmail(email);
        return findMember.orElse(null);
    }
}
