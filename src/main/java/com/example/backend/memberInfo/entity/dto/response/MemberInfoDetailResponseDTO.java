package com.example.backend.memberInfo.entity.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberInfoDetailResponseDTO {
    @NotBlank(message = "응답 이름이 비어있습니다.")
    private String nickname;

    @NotBlank(message = "응답 태그가 비어있습니다.")
    private String tag;

    private String introduction;

    private String imageUrl;

    @NotBlank(message = "응답 권한이 비어있습니다.")
    private String permission;
}
