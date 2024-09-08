package com.example.backend.memberInfo.entity.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberInfoDetailResponseDTO {
    private String nickname;

    private String introduction;

    private String imageUrl;
}
