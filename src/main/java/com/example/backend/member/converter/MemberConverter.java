package com.example.backend.member.converter;

import com.example.backend.member.entity.Member;
import com.example.backend.member.entity.dto.request.EmailVerifyRequestDTO;
import com.example.backend.member.entity.dto.request.SignupRequestDTO;
import com.example.backend.member.entity.dto.response.EmailVerifyResponseDTO;
import com.example.backend.member.entity.dto.response.JwtTokenResponseDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MemberConverter {

    // 회원가입 요청을 멤버 Entity로 변환
    public static Member signupReqeustConverter(SignupRequestDTO req){
        return Member.builder()
                .email(req.getEmail())
                .password(req.getPassword())
                .build();
    }

    // 이메일 인증 응답 변환
    public static EmailVerifyResponseDTO emailVerifyResponseConverter(EmailVerifyRequestDTO req){
        LocalDate nowDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy. MM. dd");
        LocalTime nowTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        EmailVerifyResponseDTO dto = new EmailVerifyResponseDTO();
        dto.setEmail(req.getEmail());
        dto.setCreated_time(nowDate.format(dateFormatter) + " " + nowTime.format(timeFormatter));
        return dto;
    }

    // 토큰 발급 응답 변환
    public static JwtTokenResponseDTO jwtTokenResponseConverter(String token, String expiresTime,
                                                                String refreshToken, String refreshExpiresTime){
        JwtTokenResponseDTO dto = new JwtTokenResponseDTO();
        dto.setAccess_token(token);
        dto.setExpires_in(expiresTime);
        dto.setRefresh_token(refreshToken);
        dto.setRefresh_token_expires_in(refreshExpiresTime);
        return dto;
    }

    // 비밀번호 변경
    public static Member changePasswordConverter(Member member, String newPassword){
        return Member.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(newPassword)
                .participants(member.getParticipants())
                .memberInfo(member.getMemberInfo())
                .build();
    }
}
