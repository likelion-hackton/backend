package com.example.backend.member.service;

import com.example.backend.email.EmailSenderService;
import com.example.backend.email.EmailVerifyService;
import com.example.backend.member.entity.Member;
import com.example.backend.member.entity.dto.MemberDtoConverter;
import com.example.backend.member.entity.dto.request.SignupRequestDTO;
import com.example.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if(emailVerifyService.verifyCode(req.getEmail(), req.getVerification())){
            throw new RuntimeException("유효하지 않은 인증코드");
        }
        req.setPassword(passwordEncoder.encode(req.getPassword()));
        memberRepository.save(MemberDtoConverter.signupReqeustConverter(req));
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
