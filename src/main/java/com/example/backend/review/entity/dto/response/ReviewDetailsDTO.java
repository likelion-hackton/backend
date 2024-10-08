package com.example.backend.review.entity.dto.response;

import com.example.backend.review.entity.ReviewImage;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDetailsDTO{
    //memberInfo
    @NotBlank(message = "멤버 이름이 비어있습니다.")
    private String memberNickname;

    private String memberImageUrl;

    //review
    @NotBlank(message = "리뷰 내용이 비어있습니다.")
    private String reviewComment;

    private List<ReviewImage> reviewImageUrl;

    // createdTime
    private LocalDate createdTime;
}