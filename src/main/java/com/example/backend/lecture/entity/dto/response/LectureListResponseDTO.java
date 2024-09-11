package com.example.backend.lecture.entity.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class LectureListResponseDTO {

    @NotNull(message = "강의 id가 비어있습니다.")
    private Long id;

    @NotBlank(message = "강의명이 비어있습니다.")
    private String name;

    @NotNull(message = "강의 날짜가 비어있습니다.")
    private Instant dateTime;

    @NotBlank(message = "강의유형이 비어있습니다.")
    private String type;

    @NotBlank(message = "이미지 주소가 비어있습니다.")
    private String imageUrl;
}
