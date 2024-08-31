package com.example.backend.member.controller;

import com.example.backend.member.entity.dto.request.EmailVerifyRequestDTO;
import com.example.backend.member.entity.dto.request.LoginRequestDTO;
import com.example.backend.member.entity.dto.request.RefreshRequestDTO;
import com.example.backend.member.entity.dto.request.SignupRequestDTO;
import com.example.backend.member.entity.dto.response.EmailVerifyResponseDTO;
import com.example.backend.member.entity.dto.response.JwtTokenResponseDTO;
import com.example.backend.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/send-verification")
    @ResponseBody
    public ResponseEntity<EmailVerifyResponseDTO> sendVerification(@Valid @RequestBody EmailVerifyRequestDTO req){
        EmailVerifyResponseDTO dto = memberService.sendVerificationEmail(req);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDTO req){
        memberService.signupMember(req);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<JwtTokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO req){
        return ResponseEntity.ok(memberService.login(req));
    }

    @PostMapping("/refresh")
    @ResponseBody
    public ResponseEntity<JwtTokenResponseDTO> refresh(@Valid @RequestBody RefreshRequestDTO req){
        return ResponseEntity.ok(memberService.verifyRefreshToken(req));
    }
}
