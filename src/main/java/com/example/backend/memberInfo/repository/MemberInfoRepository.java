package com.example.backend.memberInfo.repository;

import com.example.backend.memberInfo.entity.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
}
