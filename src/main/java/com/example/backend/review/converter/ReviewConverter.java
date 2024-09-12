package com.example.backend.review.converter;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.member.entity.Member;
import com.example.backend.review.entity.Review;
import com.example.backend.review.entity.dto.request.ReviewWriteRequestDTO;
import com.example.backend.review.entity.dto.response.ReviewAllDTO;
import com.example.backend.review.entity.dto.response.ReviewDetailsDTO;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewConverter {

    public static Review createReviewConverter(ReviewWriteRequestDTO request, Member member, Lecture lecture){
        return Review.builder()
                .comment(request.getReviewComment())
                .score(request.getScore())
                .created_at(LocalDateTime.now())
                .member(member)
                .lecture(lecture)
                .build();
    }

    public static ReviewDetailsDTO reviewDetailsConverter(Review review, String memberNickname){
        ReviewDetailsDTO dto = new ReviewDetailsDTO();
        dto.setReviewComment(review.getComment());
        dto.setMemberNickname(memberNickname);
        return dto;
    }

    public static List<ReviewAllDTO> reviewAllConverter(List<Review> reviews){
        return reviews.stream().map(review -> {
            ReviewAllDTO dto = new ReviewAllDTO();
            dto.setMemberNickname(review.getMember().getEmail());
            dto.setReviewComment(review.getComment());
            dto.setScore(review.getScore());
            dto.setCreatedTime(review.getCreated_at());
            return dto;
        }).toList();
    }
}
