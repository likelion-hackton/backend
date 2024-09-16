package com.example.backend.lecture.entity.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LectureBannerResponseDTO {
    @NotNull(message = "강의 ID가 비어있습니다.")
    private Long id;

    @NotBlank(message = "강의명이 비어있습니다.")
    private String name;

    private String imageUrl;
}
