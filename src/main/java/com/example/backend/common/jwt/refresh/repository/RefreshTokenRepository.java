package com.example.backend.common.jwt.refresh.repository;

import com.example.backend.common.jwt.refresh.entity.RefreshToken;
import com.example.backend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByMember(Member member);
    void deleteByMember(Member member);
}
