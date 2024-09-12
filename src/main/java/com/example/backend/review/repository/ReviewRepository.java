package com.example.backend.review.repository;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.member.entity.Member;
import com.example.backend.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByLecture(Lecture lecture);

    Review findByMemberAndLecture(Member member, Lecture lecture);
}
