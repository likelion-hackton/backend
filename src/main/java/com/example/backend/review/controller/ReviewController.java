package com.example.backend.review.controller;


import com.example.backend.review.entity.dto.response.ReviewAllDTO;
import com.example.backend.review.entity.dto.response.ReviewDetailsDTO;
import com.example.backend.review.entity.dto.request.ReviewWriteRequestDTO;
import com.example.backend.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성
    @PostMapping(value = "/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReviewDetailsDTO> writeReview(@RequestPart("review") @Valid ReviewWriteRequestDTO request,
                                                        @RequestPart("images") List<MultipartFile> images,
                                                        Authentication auth){
        // auth.getName() -> 유저 email
        return ResponseEntity.ok(reviewService.writeReview(request, images, auth.getName()));
    }

    // 디테일 리뷰 조회
    @GetMapping("/detail")
    public ResponseEntity<ReviewDetailsDTO> readReviewDetail(@RequestParam Long reviewId){
        return ResponseEntity.ok(reviewService.readReviewDetail(reviewId));
    }

    // 리뷰 전체 조회
    @GetMapping("/")
    public ResponseEntity<List<ReviewAllDTO>> readReviewList(@RequestParam Long lectureId){
        return ResponseEntity.ok(reviewService.readReviewList(lectureId));
    }

    // 좋아요


    // 싫어요


    // 클래스 평균 별점 조회
}
