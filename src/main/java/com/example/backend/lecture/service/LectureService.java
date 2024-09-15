package com.example.backend.lecture.service;

import com.example.backend.image.service.ImageService;
import com.example.backend.lecture.entity.Lecture;
import com.example.backend.lecture.entity.LectureImage;
import com.example.backend.lecture.converter.LectureConverter;
import com.example.backend.lecture.entity.OneDayLecture;
import com.example.backend.lecture.entity.RegularLecture;
import com.example.backend.lecture.entity.dto.request.CreateLectureRequestDTO;
import com.example.backend.lecture.entity.dto.request.CreateOneDayLectureRequestDTO;
import com.example.backend.lecture.entity.dto.request.CreateRegularLectureRequestDTO;
import com.example.backend.lecture.entity.dto.response.LectureDetailResponseDTO;
import com.example.backend.lecture.entity.dto.response.LectureListResponseDTO;
import com.example.backend.lecture.repository.LectureRepository;
import com.example.backend.member.entity.Member;
import com.example.backend.member.repository.MemberRepository;
import com.example.backend.participant.converter.ParticipantConverter;
import com.example.backend.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

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
        return createLectureCommon(regularLecture, member, images);
    }

    // 강의 생성 베이스
    @Transactional
    public LectureDetailResponseDTO createLectureCommon(Lecture lecture, Member member,
                                                  List<MultipartFile> images){
        // 해당 부분은 개최자 인증 전엔 일단 주석
        /*if (member.getPermission().equals("USER")){ // 일반 유저라면 X
            logger.warn("접근 권한 없음");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "접근 권한 없음");
        }*/

        imageService.procesAndAddImages(lecture, images,
                url -> {
                    LectureImage image = new LectureImage();
                    image.setImageUrl(url);
                    return image;
                }, Lecture::addImage);

        Lecture saveLecture = lectureRepository.save(lecture);
        participantRepository.save(ParticipantConverter.createParticipantConverter(member, saveLecture));
        return LectureConverter.lectureDetailConverter(saveLecture);
    }

    // 모든 강의 조회
    public List<LectureListResponseDTO> getAllLecture(){
        return lectureRepository.findAll().stream()
                .map(LectureConverter::lectureListConverter)
                .collect(Collectors.toList());
    }

    // 내가 참가한 강의 조회
    public List<LectureListResponseDTO> getMyLecture(String email){
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member == null){
            logger.warn("사용자 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 찾을 수 없음");
        }
        return participantRepository.findLecturesByMemberId(member.getId()).stream()
                .map(LectureConverter::lectureListConverter)
                .collect(Collectors.toList());
    }

    // 강의 참가
    @Transactional
    public LectureDetailResponseDTO joinLecture(Long lectureId, String email){
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
        if (participantRepository.existsByLectureAndMember(lecture, member)){
            logger.warn("이미 참가한 강의");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 참가한 강의");
        }
        if (participantRepository.countByLecture(lecture) >= lecture.getMember_limit()) {
            logger.warn("강의 정원 가득참");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "강의 정원 가득참");
        }

        participantRepository.save(ParticipantConverter.joinParticipantConverter(member, lecture));
        return LectureConverter.lectureDetailConverter(lecture);
    }

    private void setCoordinates(Lecture lecture, String address){
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/address.json")
                        .queryParam("query", address)
                        .build())
                .header("Authorization", "KakapAK " + kakaoApiKey)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    lecture.setLatitude();
                    lecture.setLongitude();
                    return lecture
                })
                .block();
    }

}
