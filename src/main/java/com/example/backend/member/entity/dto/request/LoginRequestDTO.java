package com.example.backend.member.entity.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDTO { // 로그인 요청
    @NotBlank(message = "이메일이 비어있습니다")
    private String email;

    @NotBlank(message = "비밀번호가 비어있습니다")
    private String password;
}
