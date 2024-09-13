package com.example.backend.memberInfo.repository;

import com.example.backend.memberInfo.entity.MemberInfo;
import com.example.backend.memberInfo.entity.MemberInfoImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberInfoImageRepository extends JpaRepository<MemberInfoImage, Long> {
    MemberInfoImage findByMemberInfo(MemberInfo memberInfo);

    List<MemberInfoImage> findAllByMemberInfoIn(List<MemberInfo> memberInfos);
}
