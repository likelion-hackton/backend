package com.example.backend.member.service;

import com.example.backend.email.EmailSenderService;
import com.example.backend.email.EmailVerifyService;
import com.example.backend.member.entity.Member;
import com.example.backend.member.entity.dto.MemberDtoConverter;
import com.example.backend.member.entity.dto.request.EmailVerifyRequestDTO;
import com.example.backend.member.entity.dto.request.SignupRequestDTO;
import com.example.backend.member.entity.dto.response.EmailVerifyResponseDTO;
import com.example.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional
@Primary
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final EmailSenderService emailSenderService;
    private final EmailVerifyService emailVerifyService;
    private final PasswordEncoder passwordEncoder;

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

    public Member getMemberById(Long id){
        Optional<Member> findMember = memberRepository.findById(id);
        return findMember.orElse(null);
    }

    public Member getMemberByEmail(String email){
        Optional<Member> findMember = memberRepository.findByEmail(email);
        return findMember.orElse(null);
    }
}
