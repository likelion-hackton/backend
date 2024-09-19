package com.example.backend.lecture.entity.dto.response;

import com.example.backend.category.Category;
import com.example.backend.lecture.entity.LectureImage;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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

    @NotNull(message = "시작 시간이 비어있습니다.")
    private LocalTime startTime;

    @NotNull(message = "종료 시간이 비어있습니다.")
    private LocalTime endTime;

    private LocalDate date;

    private LocalDate startDate;

    private LocalDate endDate;

    private Set<DayOfWeek> daysOfWeek;

    @NotNull(message = "위도가 비어있습니다.")
    private double latitude;

    @NotNull(message = "경도가 비어있습니다.")
    private double longitude;

    @NotBlank(message = "주소가 비어있습니다.")
    private String address;

    private String detailAddress;

    @NotBlank(message = "카테고리가 비어있습니다.")
    private Category category;

    private double averageScore;

    private Long scoreCount;

    private Long remainingSpace;

    private List<LectureImage> imageUrl;
}
