package com.example.backend.member.entity.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangePwRequestDTO {
    @NotBlank(message = "비밀번호가 비어있습니다.")
    private String newPassword;

    @NotBlank(message = "비밀번호 재입력이 비어있습니다.")
    private String checkNewPassword;
}
