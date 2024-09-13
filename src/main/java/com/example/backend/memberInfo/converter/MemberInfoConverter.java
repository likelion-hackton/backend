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
                .introduction("")
                .permission("USER")
                .member(member)
                .memberInfoImage(null)
                .build();
    }

    public static MemberInfo editMemberInfoConverter(EditMemberInfoRequestDTO req, MemberInfo existingInfo, String tag){
        existingInfo.setNickname(req.getNickname());
        existingInfo.setTag(tag);
        existingInfo.setPermission(req.getPermission());
        existingInfo.setIntroduction(req.getIntroduction() != null ? req.getIntroduction() : "");
        return existingInfo;
    }

    public static MemberInfoDetailResponseDTO memberInfoDetailConverter(MemberInfo memberInfo){
        MemberInfoDetailResponseDTO dto = new MemberInfoDetailResponseDTO();
        dto.setNickname(memberInfo.getNickname());
        dto.setTag("#" + memberInfo.getTag());
        dto.setIntroduction(memberInfo.getIntroduction() != null ? memberInfo.getIntroduction() : "");
        dto.setImageUrl(memberInfo.getMemberInfoImage() != null ? memberInfo.getMemberInfoImage().getImageUrl() : null);
        dto.setPermission(memberInfo.getPermission());
        return dto;
    }
}
