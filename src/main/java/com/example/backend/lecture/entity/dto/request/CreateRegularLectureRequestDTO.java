package com.example.backend.lecture.entity.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CreateRegularLectureRequestDTO extends CreateLectureRequestDTO {

    @NotNull(message = "시작 날짜가 비어있습니다.")
    private LocalDate startDate;

    @NotNull(message = "종료 날짜가 비어있습니다.")
    private LocalDate endDate;

    @NotEmpty(message = "강의 요일이 비어있습니다.")
    private Set<DayOfWeek> daysOfWeek;
}
