package com.example.backend.review.entity.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ReviewIdRequestDTO {
    @NotBlank
    private Long reviewId;
}
