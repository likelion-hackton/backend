package com.example.backend.review.repository;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.review.entity.Review;
import com.example.backend.review.entity.ReviewDisLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewDisLikeRepository extends JpaRepository<ReviewDisLike, Long> {
    ReviewDisLike findByReviewAndMemberId(Review review, Long memberId);

    Long countByReview(Review review);

    void deleteByReview(Review review);
}
