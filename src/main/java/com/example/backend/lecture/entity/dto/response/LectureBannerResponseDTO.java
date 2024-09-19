package com.example.backend.lecture.entity.dto.response;

import com.example.backend.category.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class LectureBannerResponseDTO {
    @NotNull(message = "강의 ID가 비어있습니다.")
    private Long id;

    @NotBlank(message = "강의 유형이 비어있습니다.")
    private String type;

    @NotBlank(message = "강의명이 비어있습니다.")
    private String name;

    @NotBlank(message = "카테고리가 비어있습니다.")
    private Category category;

    private LocalDate date;

    private LocalDate startDate;

    private LocalDate endDate;

    private String imageUrl;
}
