package com.example.backend.memberInfo.entity.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditMemberInfoRequestDTO {

    @NotBlank(message = "닉네임이 비어있습니다.")
    private String nickname;

    private String introduction;
}
