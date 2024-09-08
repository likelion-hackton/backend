package com.example.backend.memberInfo.converter;

import com.example.backend.member.entity.Member;
import com.example.backend.memberInfo.entity.MemberInfo;
import com.example.backend.memberInfo.entity.dto.request.EditMemberInfoRequestDTO;
import com.example.backend.memberInfo.entity.dto.response.MemberInfoDetailResponseDTO;

public class MemberInfoConverter {

    public static MemberInfo editMemberInfoConverter(EditMemberInfoRequestDTO req, Member member){
        return MemberInfo.builder()
                .id(member.getMemberInfo().getId())
                .member(member)
                .nickname(req.getNickname())
                .introduction(req.getIntroduction())
                .build();
    }

    public static MemberInfoDetailResponseDTO memberInfoDetailConverter(MemberInfo memberInfo){
        MemberInfoDetailResponseDTO dto = new MemberInfoDetailResponseDTO();
        dto.setNickname(memberInfo.getNickname());
        dto.setIntroduction(memberInfo.getIntroduction());
        dto.setImageUrl(memberInfo.getImage());
        return dto;
    }
}
