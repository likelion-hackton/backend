package com.example.backend.member.entity.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RefreshRequestDTO { // Refresh 요청
    @NotBlank(message = "리프레시 토큰이 비어있습니다")
    private String refresh_token;
}
