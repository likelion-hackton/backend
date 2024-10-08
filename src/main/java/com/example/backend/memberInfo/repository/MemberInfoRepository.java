package com.example.backend.memberInfo.repository;

import com.example.backend.member.entity.Member;
import com.example.backend.memberInfo.entity.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
    boolean existsByNicknameAndTag(String nickname, String tag);
    Optional<MemberInfo> findByMember(Member member);
    List<MemberInfo> findAllByMemberIn(List<Member> members);
}
