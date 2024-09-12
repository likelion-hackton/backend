package com.example.backend.review.repository;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.review.entity.Review;
import com.example.backend.review.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Long findByLectureAndMemberId(Lecture lecture, Long memberId);

    Long countByLecture(Lecture lecture);
}
