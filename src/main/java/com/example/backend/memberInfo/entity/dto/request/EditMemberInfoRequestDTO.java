package com.example.backend.memberInfo.entity.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditMemberInfoRequestDTO {

    private String nickname;

    private String introduction;

    @NotNull(message = "권한 정보가 비어있습니다.")
    private String permission;
}
