package com.example.backend.memberInfo.repository;

import com.example.backend.memberInfo.entity.MemberInfo;
import com.example.backend.memberInfo.entity.MemberInfoImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberInfoImageRepository extends JpaRepository<MemberInfoImage, Long> {
}
