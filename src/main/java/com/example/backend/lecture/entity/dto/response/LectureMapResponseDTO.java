package com.example.backend.lecture.entity.dto.response;

import com.example.backend.category.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class LectureMapResponseDTO {

    @NotNull(message = "강의 아이디가 비어있습니다.")
    private Long id;

    @NotBlank(message = "카테고리가 비어있습니다.")
    private Category category;

    @NotBlank(message = "강의명이 비어있습니다.")
    private String name;

    @NotBlank(message = "강의유형이 비어있습니다.")
    private String type;

    @NotNull(message = "가격이 비어있습니다.")
    private Long price;

    @NotNull(message = "위도가 비어있습니다.")
    private double latitude;

    @NotNull(message = "경도가 비어있습니다.")
    private double longitude;

    @NotBlank(message = "주소가 비어있습니다.")
    private String address;

    private String detailAddress;

    @NotNull(message = "시작 시간이 비어있습니다.")
    private LocalTime startTime;

    private LocalDate date;

    private LocalDate startDate;

    private LocalDate endDate;
}
