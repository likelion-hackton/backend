package com.example.backend.common.jwt.refresh.entity;

import com.example.backend.member.entity.Member;

import java.time.Instant;

public class RefreshTokenConverter {

    // 토큰 저장용 변환기
    public static RefreshToken createTokenConverter(String token, Member member, Instant expires) {
        return RefreshToken.builder()
                .refreshToken(token)
                .member(member)
                .refreshExpiresTime(expires)
                .build();
    }
}
