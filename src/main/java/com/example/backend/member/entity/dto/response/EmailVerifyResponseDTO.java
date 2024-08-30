package com.example.backend.member.entity.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailVerifyResponseDTO {
    @NotBlank(message = "이메일이 비어있습니다")
    private String email;

    @NotBlank(message = "생성시간이 비어있습니다.")
    private String created_time;
}
