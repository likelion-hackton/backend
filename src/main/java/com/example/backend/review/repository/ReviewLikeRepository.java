package com.example.backend.review.repository;


import com.example.backend.review.entity.Review;
import com.example.backend.review.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    ReviewLike findByReviewAndMemberId(Review review, Long memberId);

    Long countByReview(Review review);
}
