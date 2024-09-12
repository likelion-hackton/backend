package com.example.backend.review.service;

import com.example.backend.image.service.ImageService;
import com.example.backend.lecture.entity.Lecture;
import com.example.backend.lecture.repository.LectureRepository;
import com.example.backend.member.entity.Member;
import com.example.backend.member.repository.MemberRepository;
import com.example.backend.review.converter.ReviewConverter;
import com.example.backend.review.entity.Review;
import com.example.backend.review.entity.ReviewDisLike;
import com.example.backend.review.entity.ReviewImage;
import com.example.backend.review.entity.ReviewLike;
import com.example.backend.review.entity.dto.request.ReviewIdRequestDTO;
import com.example.backend.review.entity.dto.response.ReviewAllDTO;
import com.example.backend.review.entity.dto.response.ReviewDetailsDTO;
import com.example.backend.review.entity.dto.request.ReviewWriteRequestDTO;
import com.example.backend.review.entity.dto.response.ReviewScoreResponseDTO;
import com.example.backend.review.repository.ReviewDisLikeRepository;
import com.example.backend.review.repository.ReviewLikeRepository;
import com.example.backend.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.example.backend.review.converter.ReviewConverter.reviewAllConverter;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    // private final MemberInfoRepository memberInfoRepository; -> 멤버 인포 레포 필요
    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewDisLikeRepository reviewDisLikeRepository;

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
        return ReviewConverter.reviewDetailsConverter(review, review.getMember().getEmail());
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
        return reviewAllConverter(reviewList);
    }

    // 클래스 평균 별점 조회
    public ReviewScoreResponseDTO getReviewAvgScore(Long lectureId) {
        // lecture id -> lecture 찾기
        Lecture lecture = lectureRepository.findById(lectureId).orElse(null);
        if (lecture == null) {
            logger.warn("존재하지 않는 클래스입니다.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 클래스입니다.");
        }

        // 1. lecture 에 해당하는 review list 가져오기
        List<Review> reviewList = reviewRepository.findAllByLecture(lecture);

        // 2. review list 에서 score 를 가져와서 평균 계산
        double averageScore = reviewList.stream()
                .mapToLong(Review::getScore)
                .average()
                .orElse(0);

        // 3. 별점 5점~1점 비율 계산
        long scoreFive = reviewList.stream().filter(review -> review.getScore() == 5).count();
        long scoreFour = reviewList.stream().filter(review -> review.getScore() == 4).count();
        long scoreThree = reviewList.stream().filter(review -> review.getScore() == 3).count();
        long scoreTwo = reviewList.stream().filter(review -> review.getScore() == 2).count();
        long scoreOne = reviewList.stream().filter(review -> review.getScore() == 1).count();

        // 4. 총 리뷰 개수
        long totalReviewCount = reviewList.size();

        scoreFive = scoreFive * 100 / totalReviewCount;
        scoreFour = scoreFour * 100 / totalReviewCount;
        scoreThree = scoreThree * 100 / totalReviewCount;
        scoreTwo = scoreTwo * 100 / totalReviewCount;
        scoreOne = scoreOne * 100 / totalReviewCount;

        // 5. response DTO 생성
        ReviewScoreResponseDTO responseDTO = new ReviewScoreResponseDTO();
        responseDTO.setAverageScore((long) averageScore);
        responseDTO.setScoreFive(scoreFive);
        responseDTO.setScoreFour(scoreFour);
        responseDTO.setScoreThree(scoreThree);
        responseDTO.setScoreTwo(scoreTwo);
        responseDTO.setScoreOne(scoreOne);
        responseDTO.setTotalReviewCount(totalReviewCount);

        return responseDTO;
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(ReviewIdRequestDTO request, String email) {
        // member email -> member 찾기
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) {
            logger.warn("존재하지 않는 회원입니다.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.");
        }

        // review id -> review 찾기
        Review review = reviewRepository.findById(request.getReviewId()).orElse(null);
        if (review == null) {
            logger.warn("존재하지 않는 리뷰입니다.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다.");
        }

        // 리뷰 작성자와 삭제 요청자가 다르면 예외처리
        if (!review.getMember().equals(member)) {
            logger.warn("리뷰 삭제 권한이 없습니다.");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "리뷰 삭제 권한이 없습니다.");
        }

        // review 삭제
        reviewRepository.delete(review);
    }

    // 좋아요
    @Transactional
    public Long likeReview(Long LectureID, String email) {
        // member email -> member 찾기
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) {
            logger.warn("존재하지 않는 회원입니다.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.");
        }
        Long memberId = member.getId();

        // lecture id -> lecture 찾기
        Lecture lecture = lectureRepository.findById(LectureID).orElse(null);
        if (lecture == null) {
            logger.warn("존재하지 않는 클래스입니다.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 클래스입니다.");
        }

        // 이미 좋아요를 눌렀으면 예외처리
        if (reviewLikeRepository.findByLectureAndMemberId(lecture, memberId) != null) {
            logger.warn("이미 좋아요를 누른 리뷰입니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 리뷰입니다.");
        }

        // reviewLike 생성
        ReviewLike reviewLike = ReviewLike.builder()
                .lecture(lecture)
                .memberId(memberId)
                .build();

        // reviewLike 저장
        reviewLikeRepository.save(reviewLike);

        // review 좋아요 수 세기
        return reviewLikeRepository.countByLecture(lecture);
    }

    // 싫어요
    @Transactional
    public Long disLikeReview(Long lectureId, String email) {
        // member email -> member 찾기
        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) {
            logger.warn("존재하지 않는 회원입니다.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.");
        }
        Long memberId = member.getId();

        // lecture id -> lecture 찾기
        Lecture lecture = lectureRepository.findById(lectureId).orElse(null);
        if (lecture == null) {
            logger.warn("존재하지 않는 클래스입니다.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 클래스입니다.");
        }

        // 이미 싫어요를 눌렀으면 예외처리
        if (reviewDisLikeRepository.findByLectureAndMemberId(lecture, memberId) != null) {
            logger.warn("이미 싫어요를 누른 리뷰입니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 싫어요를 누른 리뷰입니다.");
        }

        // reviewDisLike 생성
        ReviewDisLike reviewDisLike = ReviewDisLike.builder()
                .lecture(lecture)
                .memberId(memberId)
                .build();

        // reviewDisLike 저장
        reviewDisLikeRepository.save(reviewDisLike);

        // review 싫어요 수 세기
        return reviewDisLikeRepository.countByLecture(lecture);
    }
}
