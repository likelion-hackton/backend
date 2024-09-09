package com.example.backend.memberInfo.repository;

import com.example.backend.member.entity.Member;
import com.example.backend.memberInfo.entity.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
    boolean existsByNicknameAndTag(String nickname, String tag);
    Optional<MemberInfo> findByMember(Member member);
}
