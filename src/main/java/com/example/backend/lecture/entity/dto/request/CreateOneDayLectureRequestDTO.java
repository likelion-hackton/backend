package com.example.backend.lecture.entity.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CreateOneDayLectureRequestDTO extends CreateLectureRequestDTO {
    @NotNull(message = "강의 날짜가 비어있습니다.")
    private LocalDate date;
}
