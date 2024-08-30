package com.example.backend.member.entity.dto;

import com.example.backend.member.entity.Member;
import com.example.backend.member.entity.dto.request.SignupRequestDTO;

public class MemberDtoConverter {

    public static Member signupReqeustConverter(SignupRequestDTO req){
        return Member.builder()
                .email(req.getEmail())
                .password(req.getPassword())
                .role("USER")
                .build();
    }
}
