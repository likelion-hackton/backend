package com.example.backend.lecture.entity.dto.response;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class LectureDetailResponseDTO {

    @NotNull(message = "강의 아이디가 비어있습니다.")
    private Long id;

    @NotBlank(message = "강의명이 비어있습니다.")
    private String name;

    private String description;

    @NotNull(message = "가격이 비어있습니다.")
    private Long price;

    @NotBlank(message = "강의 유형이 비어있습니다.")
    private String type;

    @NotNull(message = "최대인원이 비어있습니다.")
    private int member_limit;

    private Instant dateTime;

    private Instant startDate;

    private Instant endDate;

    private LocalTime time;

    private Set<DayOfWeek> daysOfWeek;

    @NotBlank(message = "강의 위치가 비어있습니다.")
    private String location;
}
