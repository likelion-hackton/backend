package com.example.backend.memberInfo.converter;

import com.example.backend.member.entity.Member;
import com.example.backend.memberInfo.entity.MemberInfo;
import com.example.backend.memberInfo.entity.dto.request.EditMemberInfoRequestDTO;
import com.example.backend.memberInfo.entity.dto.response.MemberInfoDetailResponseDTO;

public class MemberInfoConverter {

    public static MemberInfo initMemberInfoConverter(Member member, String tag){
        return MemberInfo.builder()
                .nickname("사용자")
                .tag(tag)
                .member(member)
                .memberInfoImage(null)
                .build();
    }

    public static MemberInfo editMemberInfoConverter(EditMemberInfoRequestDTO req, Member member, String tag){
        return MemberInfo.builder()
                .id(member.getMemberInfo().getId())
                .member(member)
                .nickname(req.getNickname())
                .tag(tag)
                .introduction(req.getIntroduction())
                .build();
    }

    public static MemberInfoDetailResponseDTO memberInfoDetailConverter(MemberInfo memberInfo){
        MemberInfoDetailResponseDTO dto = new MemberInfoDetailResponseDTO();
        dto.setNickname(memberInfo.getNickname() + "#" + memberInfo.getTag());
        dto.setIntroduction(memberInfo.getIntroduction());
        dto.setImageUrl(memberInfo.getMemberInfoImage().getImageUrl());
        return dto;
    }
}
