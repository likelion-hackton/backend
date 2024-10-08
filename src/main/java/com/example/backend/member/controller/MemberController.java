package com.example.backend.member.controller;

import com.example.backend.member.entity.dto.request.*;
import com.example.backend.member.entity.dto.response.EmailVerifyResponseDTO;
import com.example.backend.member.entity.dto.response.JwtTokenResponseDTO;
import com.example.backend.member.service.MemberService;
import com.example.backend.memberInfo.service.MemberInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;
    private final MemberInfoService memberInfoService;

    // 인증메일 전송
    @PostMapping("/send-verification")
    @ResponseBody
    public ResponseEntity<EmailVerifyResponseDTO> sendVerification(@Valid @RequestBody EmailVerifyRequestDTO req){
        EmailVerifyResponseDTO dto = memberService.sendVerificationEmail(req);
        return ResponseEntity.ok(dto);
    }

    // 인증코드 검증
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@Valid @RequestBody VerifyCodeRequestDTO req){
        memberService.verifyCode(req);
        return ResponseEntity.ok("인증성공");
    }

    // 회원가입
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDTO req){
        memberService.signupMember(req);
        memberInfoService.initMemberInfo(req.getEmail());
        return ResponseEntity.ok("회원가입 성공");
    }

    // 로그인
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<JwtTokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO req){
        return ResponseEntity.ok(memberService.login(req));
    }

    // 로그아웃
    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<String> logout(Authentication auth){
        memberService.logout(auth.getName());
        return ResponseEntity.ok("로그아웃 성공");
    }

    // 토큰 리프레시
    @PostMapping("/refresh")
    @ResponseBody
    public ResponseEntity<JwtTokenResponseDTO> refresh(@Valid @RequestBody RefreshRequestDTO req){
        return ResponseEntity.ok(memberService.verifyRefreshToken(req));
    }

    // 비밀번호 변경
    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePwRequestDTO req, Authentication auth){
        memberService.changePassword(req, auth.getName());
        return ResponseEntity.ok("비밀번호 변경 성공");
    }

}
