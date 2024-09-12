package com.example.backend.review.entity.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewScoreResponseDTO {

    @NotNull(message = "리뷰 평균 점수가 비어있습니다.")
    private Long averageScore;

    @NotNull(message = "리뷰 점수 5점 비율이 비어있습니다.")
    private Long scoreFive;

    @NotNull(message = "리뷰 점수 4점 비율이 비어있습니다.")
    private Long scoreFour;

    @NotNull(message = "리뷰 점수 3점 비율이 비어있습니다.")
    private Long scoreThree;

    @NotNull(message = "리뷰 점수 2점 비율이 비어있습니다.")
    private Long scoreTwo;

    @NotNull(message = "리뷰 점수 1점 비율이 비어있습니다.")
    private Long scoreOne;

    @NotNull(message = "리뷰 총 개수가 비어있습니다.")
    private Long totalReviewCount;
}
