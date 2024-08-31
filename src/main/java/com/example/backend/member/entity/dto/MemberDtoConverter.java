package com.example.backend.member.entity.dto;

import com.example.backend.member.entity.Member;
import com.example.backend.member.entity.dto.request.EmailVerifyRequestDTO;
import com.example.backend.member.entity.dto.request.SignupRequestDTO;
import com.example.backend.member.entity.dto.response.EmailVerifyResponseDTO;
import com.example.backend.member.entity.dto.response.JwtTokenResponseDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MemberDtoConverter {

    public static Member signupReqeustConverter(SignupRequestDTO req){
        return Member.builder()
                .email(req.getEmail())
                .password(req.getPassword())
                .role("USER")
                .build();
    }

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

    public static JwtTokenResponseDTO jwtTokenResponseConverter(String token, String expiresTime,
                                                                String refreshToken, String refreshExpiresTime){
        JwtTokenResponseDTO dto = new JwtTokenResponseDTO();
        dto.setAccess_token(token);
        dto.setExpires_in(expiresTime);
        dto.setRefresh_token(refreshToken);
        dto.setRefresh_token_expires_in(refreshExpiresTime);
        return dto;
    }
}
