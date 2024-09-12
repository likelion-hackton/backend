package com.example.backend.review.entity.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReviewAllDTO {
    @NotBlank(message = "멤버 이름이 비어있습니다.")
    private String memberNickname;

    private String memberImageUrl;

    @NotNull(message = "리뷰 내용이 비어있습니다.")
    private String reviewComment;

    @NotNull(message = "리뷰 점수가 비어있습니다.")
    private Long score;

    @NotNull(message = "리뷰 작성 시간이 비어있습니다.")
    private LocalDateTime createdTime;

    @NotNull(message = "리뷰 좋아요 수가 비어있습니다.")
    private Long likeCount;

    @NotNull(message = "리뷰 싫어요 수가 비어있습니다.")
    private Long dislikeCount;


}
