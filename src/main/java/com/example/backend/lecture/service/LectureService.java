package com.example.backend.lecture.service;

import com.example.backend.category.Category;
import com.example.backend.image.service.ImageService;
import com.example.backend.lecture.entity.*;
import com.example.backend.lecture.converter.LectureConverter;
import com.example.backend.lecture.entity.dto.request.CreateLectureRequestDTO;
import com.example.backend.lecture.entity.dto.request.CreateOneDayLectureRequestDTO;
import com.example.backend.lecture.entity.dto.request.CreateRegularLectureRequestDTO;
import com.example.backend.lecture.entity.dto.response.LectureBannerResponseDTO;
import com.example.backend.lecture.entity.dto.response.LectureDetailResponseDTO;
import com.example.backend.lecture.entity.dto.response.LectureListResponseDTO;
import com.example.backend.lecture.entity.dto.response.LectureMapResponseDTO;
import com.example.backend.lecture.repository.LectureRepository;
import com.example.backend.member.entity.Member;
import com.example.backend.member.repository.MemberRepository;
import com.example.backend.participant.converter.ParticipantConverter;
import com.example.backend.participant.repository.ParticipantRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;
    private final ParticipantRepository participantRepository;
    private final ImageService imageService;
    private final WebClient webClient;

    @Value("${spring.kakao.api.key}")
    private String kakaoApiKey;

    // Json 처리용
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(LectureService.class);

    // 원데이 강의 생성
    @Transactional
    public LectureDetailResponseDTO createOneDayLecture(CreateOneDayLectureRequestDTO req, String email,
                                                        List<MultipartFile> images) {
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member == null){ // 없는 사용자라면 X
            logger.warn("사용자 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 찾을 수 없음");
        }
        OneDayLecture oneDayLecture = LectureConverter.createOneDayLectureConverter(req);
        setCoordinates(oneDayLecture, req.getAddress());
        return createLectureCommon(oneDayLecture, member, images);
    }

    // 정규 강의 생성
    @Transactional
    public LectureDetailResponseDTO createRegularLecture(CreateRegularLectureRequestDTO req, String email,
                                                         List<MultipartFile> images) {
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member == null){ // 없는 사용자라면 X
            logger.warn("사용자 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 찾을 수 없음");
        }
        RegularLecture regularLecture = LectureConverter.createRegularLectureConverter(req);
        setCoordinates(regularLecture, req.getAddress());
        return createLectureCommon(regularLecture, member, images);
    }

    // 강의 생성 베이스
    @Transactional
    public LectureDetailResponseDTO createLectureCommon(Lecture lecture, Member member,
                                                  List<MultipartFile> images){
        // 해당 부분은 개최자 인증 전엔 일단 주석
        if (!member.getMemberInfo().getPermission().equals("CREATOR")){ // 일반 유저라면 X
            logger.warn("접근 권한 없음");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "접근 권한 없음");
        }

        if (images!=null && !images.isEmpty()){
            imageService.procesAndAddImages(lecture, images,
                    url -> {
                        LectureImage image = new LectureImage();
                        image.setImageUrl(url);
                        return image;
                    }, Lecture::addImage);
        }
        Lecture saveLecture = lectureRepository.save(lecture);
        participantRepository.save(ParticipantConverter.createParticipantConverter(member, saveLecture));
        return LectureConverter.lectureDetailConverter(saveLecture, participantRepository.sumMemberCountByLectureAndRole(lecture, "USER"));
    }

    // 강의 상세 조회
    public LectureDetailResponseDTO lectureDetail(Long lecture_id){
        Lecture lecture = lectureRepository.findById(lecture_id).orElse(null);
        if(lecture == null){
            logger.warn("강의 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "강의 찾을 수 없음");
        }
        return LectureConverter.lectureDetailConverter(lecture, participantRepository.sumMemberCountByLectureAndRole(lecture, "USER"));
    }

    // 배너 강의 조회
    public List<LectureBannerResponseDTO> lectureBanner(){
        PageRequest topThree = PageRequest.of(0, 3);
        List<Lecture> lectures = lectureRepository.findTop3ByOrderByViewCountDesc(topThree);
        return lectures.stream()
                .map(LectureConverter::lectureBannerConverter)
                .collect(Collectors.toList());
    }

    // 카테고리로 강의 조회
    public List<LectureListResponseDTO> getLectureByCategory(String categoryName){
        Category category;
        try {
            category = Category.valueOf(categoryName.toUpperCase());
        } catch (IllegalArgumentException e){
            logger.warn("카테고리 존재하지 않음");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카테고리 존재하지 않음");
        }

        if (category == Category.ALL){
            return lectureRepository.findAll().stream()
                    .map(LectureConverter::lectureListConverter)
                    .collect(Collectors.toList());
        }
        return lectureRepository.findByCategory(category).stream()
                .map(LectureConverter::lectureListConverter)
                .collect(Collectors.toList());
    }

    // 내가 참가한 강의 조회
    public List<LectureListResponseDTO> getMyLecture(String email, String permission){
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member == null){
            logger.warn("사용자 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 찾을 수 없음");
        }
        return participantRepository.findLecturesByMemberIdAndRole(member.getId(), permission.toUpperCase()).stream()
                .map(LectureConverter::lectureListConverter)
                .collect(Collectors.toList());
    }

    // 텍스트로 강의 조회
    @Transactional
    public List<LectureListResponseDTO> searchLectureByKeyword(String keyword){
        List<Lecture> findLectures = lectureRepository.findByNameContainingOrDescriptionContaining(keyword);
        for (Lecture lecture : findLectures){
            if (lecture.getLectureCount() == null){
                lecture.setLectureCount(LectureCount.builder().lecture(lecture).viewCount(0L).build());
                lectureRepository.save(lecture);
            }
            lectureRepository.incrementViewCount(lecture.getId());
            lectureRepository.flush(); // 변경 사항 즉시 반영
        }

        findLectures = lectureRepository.findAllById(findLectures.stream().map(Lecture::getId).collect(Collectors.toList()));

        return findLectures.stream()
                .map(LectureConverter::lectureListConverter)
                .collect(Collectors.toList());
    }

    public List<LectureMapResponseDTO> getLectureMap(){
        return lectureRepository.findAll().stream()
                .map(LectureConverter::lectureMapConverter)
                .collect(Collectors.toList());
    }

    // 강의 참가
    @Transactional
    public LectureDetailResponseDTO joinLecture(Long lectureId, Long count, String email){
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member == null){
            logger.warn("사용자 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 찾을 수 없음");
        }
        Lecture lecture = lectureRepository.findById(lectureId).orElse(null);
        if(lecture == null){
            logger.warn("강의 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "강의 찾을 수 없음");
        }
        if (participantRepository.existsByLectureAndMemberAndRole(lecture, member, "USER")){
            logger.warn("이미 참가한 강의");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 참가한 강의");
        } else if (participantRepository.existsByLectureAndMemberAndRole(lecture, member, "CREATOR")){
            logger.warn("내가 개최한 강의");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "내가 개최한 강의");
        }
        Long userCount = participantRepository.sumMemberCountByLectureAndRole(lecture, "USER");
        if (userCount == null){
            if (count > lecture.getMember_limit()) {
                logger.warn("강의 정원 가득참");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "강의 정원 가득참");
            }
        } else if (userCount + count > lecture.getMember_limit()){
            logger.warn("강의 정원 가득참");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "강의 정원 가득참");
        }

        participantRepository.save(ParticipantConverter.joinParticipantConverter(member, lecture, count));
        return LectureConverter.lectureDetailConverter(lecture, participantRepository.sumMemberCountByLectureAndRole(lecture, "USER"));
    }

    @Transactional
    public void deleteLecture(String email, Long lectureId){
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member == null){
            logger.warn("사용자 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 찾을 수 없음");
        }
        Lecture lecture = lectureRepository.findById(lectureId).orElse(null);
        if(lecture == null){
            logger.warn("강의 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "강의 찾을 수 없음");
        }
        if (!participantRepository.existsByLectureAndMemberAndRole(lecture, member, "CREATOR")){
            logger.warn("삭제 권한 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제 권한 없음");
        }
        lectureRepository.delete(lecture);
    }

    private void setCoordinates(Lecture lecture, String address){
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/address.json")
                        .queryParam("query", address)
                        .build())
                .header("Authorization", "KakaoAK " + kakaoApiKey)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    double latitude = extractLatitude(response);
                    double longitude = extractLongitude(response);
                    lecture.setLatitude(latitude);
                    lecture.setLongitude(longitude);
                    return lecture;
                })
                .block();
    }

    private double extractLatitude(String response){
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode documents = root.path("documents");
            if (documents.isArray() && !documents.isEmpty()){
                JsonNode firstResult = documents.get(0);
                return firstResult.path("y").asDouble();
            }
            logger.warn("위도 정보 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "위도 정보 찾을 수 없음");
        } catch (IOException e){
            logger.warn("JSON 파싱 중 오류 발생");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 파싱 중 오류 발생");
        }
    }

    private double extractLongitude(String response){
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode documents = root.path("documents");
            if (documents.isArray() && !documents.isEmpty()){
                JsonNode firstResult = documents.get(0);
                return firstResult.path("x").asDouble();
            }
            logger.warn("경도 정보 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "경도 정보 찾을 수 없음");
        } catch (IOException e){
            logger.warn("JSON 파싱 중 오류 발생");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 파싱 중 오류 발생");
        }
    }

}
