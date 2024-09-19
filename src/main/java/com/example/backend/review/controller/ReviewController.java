package com.example.backend.review.controller;


import com.example.backend.review.entity.dto.request.ReviewIdRequestDTO;
import com.example.backend.review.entity.dto.response.ReviewAllDTO;
import com.example.backend.review.entity.dto.response.ReviewDetailsDTO;
import com.example.backend.review.entity.dto.request.ReviewWriteRequestDTO;
import com.example.backend.review.entity.dto.response.ReviewScoreResponseDTO;
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
    public ResponseEntity<ReviewDetailsDTO> readReviewDetail(@RequestParam("reviewID") Long reviewId){
        return ResponseEntity.ok(reviewService.readReviewDetail(reviewId));
    }

    // 리뷰 전체 조회
    @GetMapping("/")
    public ResponseEntity<List<ReviewAllDTO>> readReviewList(@RequestParam("lectureId") Long lectureId){
        return ResponseEntity.ok(reviewService.readReviewList(lectureId));
    }

    // 리뷰 평균 별점 조회
    @GetMapping("/score/average")
    public ResponseEntity<ReviewScoreResponseDTO> readReviewAverageScore(@RequestParam("lectureId") Long lectureId){
        return ResponseEntity.ok(reviewService.getReviewAvgScore(lectureId));
    }

    // 리뷰 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteReview(@RequestBody ReviewIdRequestDTO request, Authentication auth){
        reviewService.deleteReview(request, auth.getName());
        return ResponseEntity.ok("리뷰 삭제 성공");
    }

    // 좋아요 선택
    @PostMapping("/like")
    public ResponseEntity<Long> likeReview(@RequestParam("reviewId") Long reviewId, Authentication auth){
        return ResponseEntity.ok(reviewService.likeReview(reviewId, auth.getName()));
    }

    // 싫어요 선택
    @PostMapping("/disLike")
    public ResponseEntity<Long> disLikeReview(@RequestParam("reviewId") Long reviewId, Authentication auth){
        return ResponseEntity.ok(reviewService.disLikeReview(reviewId, auth.getName()));
    }

    // 좋아요 취소
    @DeleteMapping("/like")
    public ResponseEntity<Long> cancelLikeReview(@RequestParam("reviewId") Long reviewId, Authentication auth){
        return ResponseEntity.ok(reviewService.cancelLikeReview(reviewId, auth.getName()));
    }

    // 싫어요 취소
    @DeleteMapping("/disLike")
    public ResponseEntity<Long> cancelDisLikeReview(@RequestParam("reviewId") Long reviewId, Authentication auth){
        return ResponseEntity.ok(reviewService.cancelDisLikeReview(reviewId, auth.getName()));
    }
}
