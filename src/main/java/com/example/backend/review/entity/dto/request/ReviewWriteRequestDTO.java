package com.example.backend.review.entity.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewWriteRequestDTO {
    // 유저 -> authuser 에서 가져오기

    @NotBlank(message = "리뷰 내용이 비어있습니다.")
    private String reviewComment;

    @NotNull(message = "리뷰 점수가 비어있습니다.")
    private Long score;

    @NotNull(message = "강의 아이디가 비어있습니다.")
    private Long lectureId;
}
