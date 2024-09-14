package com.example.backend.lecture.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@DiscriminatorValue("OneDay")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OneDayLecture extends Lecture{

    @NotNull(message = "날짜가 비어있습니다.")
    private Instant dataTime;
}
