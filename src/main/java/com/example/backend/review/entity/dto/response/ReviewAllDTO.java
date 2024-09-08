package com.example.backend.review.entity.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReviewAllDTO {


    private String memberNickname;

    private String memberImage;

    private String reviewComment;

    private Long score;

    private LocalDateTime createdTime;

    private Long likeCount;

    private Long dislikeCount;


}
