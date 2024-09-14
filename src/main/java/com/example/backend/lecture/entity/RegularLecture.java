package com.example.backend.lecture.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Set;

@Entity
@DiscriminatorValue("Regular")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegularLecture extends Lecture{

    @NotNull(message = "시작 날짜가 비어있습니다.")
    private Instant startDate;

    @NotNull(message = "종료 날짜가 비어있습니다.")
    private Instant endDate;

    @NotNull(message = "강의 시간이 비어있습니다.")
    private LocalTime time;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> daysOfWeek;
}
