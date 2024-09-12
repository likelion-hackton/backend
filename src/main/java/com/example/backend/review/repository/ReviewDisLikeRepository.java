package com.example.backend.review.repository;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.review.entity.ReviewDisLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewDisLikeRepository extends JpaRepository<ReviewDisLike, Long> {
    Long findByLectureAndMemberId(Lecture lecture, Long memberId);

    Long countByLecture(Lecture lecture);
}
