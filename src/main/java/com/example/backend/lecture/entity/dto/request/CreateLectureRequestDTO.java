package com.example.backend.lecture.entity.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class CreateLectureRequestDTO {
    @NotBlank(message = "강의명이 비어있습니다.")
    private String name;

    private String description;

    @NotNull(message = "가격이 비어있습니다.")
    private Long price;

    @NotNull(message = "최대인원이 비어있습니다.")
    private int member_limit;

    @NotBlank(message = "강의 위치가 비어있습니다.")
    private String location;
}
