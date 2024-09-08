package com.example.backend.review.service;

import com.example.backend.image.service.ImageService;
import com.example.backend.lecture.entity.Lecture;
import com.example.backend.lecture.repository.LectureRepository;
import com.example.backend.member.entity.Member;
import com.example.backend.member.repository.MemberRepository;
import com.example.backend.review.converter.ReviewConverter;
import com.example.backend.review.entity.Review;
import com.example.backend.review.entity.ReviewImage;
import com.example.backend.review.entity.dto.response.ReviewAllDTO;
import com.example.backend.review.entity.dto.response.ReviewDetailsDTO;
import com.example.backend.review.entity.dto.request.ReviewWriteRequestDTO;
import com.example.backend.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    // private final MemberInfoRepository memberInfoRepository; -> 멤버 인포 레포 필요

    private final ImageService imageService;

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);


    // 리뷰 작성
    @Transactional
    public ReviewDetailsDTO writeReview(ReviewWriteRequestDTO request,
                                        List<MultipartFile> images , String email) {
        // member email -> member info 찾기
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) {
            logger.warn("존재하지 않는 회원입니다.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.");
        }

        // lecture id -> lecture 찾기
        Lecture lecture = lectureRepository.findById(request.getLectureId()).orElse(null);
        if (lecture == null) {
            logger.warn("존재하지 않는 강의입니다.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 클래스입니다.");
        }

        // 이미 작성한 적이 있으면 예외처리
        if (reviewRepository.findByMemberAndLecture(member, lecture) != null) {
            logger.warn("이미 리뷰를 작성한 회원입니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 리뷰를 작성한 회원입니다.");
        }

        // review 생성
        Review review = ReviewConverter.createReviewConverter(request, member, lecture);

        // image 저장
        imageService.procesAndAddImages(review, images,
                url -> {
                    ReviewImage image = new ReviewImage();
                    image.setImageUrl(url);
                    return image;
                }, Review::addImage);

        // review 저장
        reviewRepository.save(review);

        // memberInfo 에서 필요한 정보 가져오기
        String memberNickname = member.getEmail(); // 일단 info 가 없으니 email로 대체


        return ReviewConverter.reviewDetailsConverter(review, memberNickname);
    }

    // 리뷰 상세 조회
    public ReviewDetailsDTO readReviewDetail(Long reviewId) {
        // review id -> review 찾기
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null) {
            logger.warn("존재하지 않는 리뷰입니다.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다.");
        }


        // response DTO 생성
        ReviewDetailsDTO reviewDetailsDTO = new ReviewDetailsDTO();
        reviewDetailsDTO.setReviewComment(review.getComment());
        reviewDetailsDTO.setMemberNickname(review.getMember().getEmail());

        return reviewDetailsDTO;
    }

    // 리뷰 전체 조회
    public List<ReviewAllDTO> readReviewList(Long lectureId) {
        // lecture id -> lecture 찾기
        Lecture lecture = lectureRepository.findById(lectureId).orElse(null);
        if (lecture == null) {
            logger.warn("존재하지 않는 클래스입니다.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 클래스입니다.");
        }

        // lecture 에 해당하는 review list 가져오기
        List<Review> reviewList = reviewRepository.findAllByLecture(lecture);

        // response DTO list 생성
        List<ReviewAllDTO> reviewDetailsDTOList = new ArrayList<>();  // 리스트 초기화
        for (Review review : reviewList) {
            ReviewAllDTO reviewAllDTO = new ReviewAllDTO();
            reviewAllDTO.setReviewComment(review.getComment());
            reviewAllDTO.setMemberNickname(review.getMember().getEmail());
            reviewDetailsDTOList.add(reviewAllDTO);
        }

        return reviewDetailsDTOList;
    }

}
