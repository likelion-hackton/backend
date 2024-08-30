package com.example.backend.member.service;

import com.example.backend.email.EmailSenderService;
import com.example.backend.email.EmailVerifyService;
import com.example.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Primary
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final EmailSenderService emailSenderService;
    private final EmailVerifyService emailVerifyService;
    private final PasswordEncoder passwordEncoder;
}
