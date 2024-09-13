package com.example.backend.review.converter;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.member.entity.Member;
import com.example.backend.review.entity.Review;
import com.example.backend.review.entity.dto.request.ReviewWriteRequestDTO;
import com.example.backend.review.entity.dto.response.ReviewDetailsDTO;

import java.time.LocalDateTime;

public class ReviewConverter {

    public static Review createReviewConverter(ReviewWriteRequestDTO request, Member member, Lecture lecture){
        return Review.builder()
                .comment(request.getReviewComment())
                .score(request.getScore())
                .created_at(LocalDateTime.now())
                .member(member)
                .lecture(lecture)
                .likeCount(0L)
                .dislikeCount(0L)
                .build();
    }

    public static ReviewDetailsDTO reviewDetailsConverter(Review review, String memberNickname, String memberImageUrl){
        ReviewDetailsDTO dto = new ReviewDetailsDTO();
        dto.setReviewComment(review.getComment());
        dto.setMemberNickname(memberNickname);
        dto.setMemberImageUrl(memberImageUrl);
        return dto;
    }
}
