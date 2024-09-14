package com.example.backend.lecture.entity.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CreateRegularLectureRequestDTO extends CreateLectureRequestDTO {
    @NotNull(message = "시작 날짜가 비어있습니다.")
    private Instant startDate;

    @NotNull(message = "종료 날짜가 비어있습니다.")
    private Instant endDate;

    @NotNull(message = "강의 시간이 비어있습니다.")
    private LocalTime time;

    @NotEmpty(message = "강의 요일이 비어있습니다.")
    private Set<DayOfWeek> daysOfWeek;
}
