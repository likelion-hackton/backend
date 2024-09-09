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
                .member(member)
                .memberInfoImage(null)
                .build();
    }

    public static MemberInfo editMemberInfoConverter(EditMemberInfoRequestDTO req, MemberInfo existingInfo, String tag){
        existingInfo.setNickname(req.getNickname());
        existingInfo.setTag(tag);
        existingInfo.setIntroduction(req.getIntroduction() != null ? req.getIntroduction() : "");
        return existingInfo;
    }

    public static MemberInfoDetailResponseDTO memberInfoDetailConverter(MemberInfo memberInfo){
        MemberInfoDetailResponseDTO dto = new MemberInfoDetailResponseDTO();
        dto.setNickname(memberInfo.getNickname() + "#" + memberInfo.getTag());
        dto.setIntroduction(memberInfo.getIntroduction() != null ? memberInfo.getIntroduction() : "");
        dto.setImageUrl(memberInfo.getMemberInfoImage() != null ? memberInfo.getMemberInfoImage().getImageUrl() : null);
        return dto;
    }
}
