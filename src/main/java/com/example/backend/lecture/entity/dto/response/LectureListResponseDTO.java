package com.example.backend.lecture.entity.dto.response;

import com.example.backend.lecture.entity.LectureImage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LectureListResponseDTO {

    @NotNull(message = "강의 id가 비어있습니다.")
    private Long id;

    @NotBlank(message = "강의명이 비어있습니다.")
    private String name;

    @NotBlank(message = "강의유형이 비어있습니다.")
    private String type;

    @NotNull(message = "강의 가격이 비어있습니다.")
    private Long price;

    private List<LectureImage> imageUrl;
}
