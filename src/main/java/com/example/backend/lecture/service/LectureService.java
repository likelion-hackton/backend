package com.example.backend.lecture.service;

import com.example.backend.image.service.ImageService;
import com.example.backend.lecture.entity.Lecture;
import com.example.backend.lecture.entity.LectureImage;
import com.example.backend.lecture.converter.LectureConverter;
import com.example.backend.lecture.entity.dto.request.CreateLectureRequestDTO;
import com.example.backend.lecture.entity.dto.response.LectureDetailResponseDTO;
import com.example.backend.lecture.repository.LectureRepository;
import com.example.backend.member.entity.Member;
import com.example.backend.member.repository.MemberRepository;
import com.example.backend.participant.entity.dto.ParticipantDtoConverter;
import com.example.backend.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;
    private final ParticipantRepository participantRepository;

    private final ImageService imageService;

    private static final Logger logger = LoggerFactory.getLogger(LectureService.class);

    // 강의 생성
    @Transactional
    public LectureDetailResponseDTO createLecture(CreateLectureRequestDTO req, String email,
                                                  List<MultipartFile> images){
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member == null){ // 없는 사용자라면 X
            logger.warn("사용자 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 찾을 수 없음");
        }
        // 해당 부분은 개최자 인증 전엔 일단 주석
        /*if (member.getPermission().equals("USER")){ // 일반 유저라면 X
            logger.warn("접근 권한 없음");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "접근 권한 없음");
        }*/
        Lecture lecture = LectureConverter.createLectureConverter(req);

        imageService.procesAndAddImages(lecture, images,
                url -> {
                    LectureImage image = new LectureImage();
                    image.setImageUrl(url);
                    return image;
                }, Lecture::addImage);

        Lecture saveLecture = lectureRepository.save(lecture);
        participantRepository.save(ParticipantDtoConverter.createParticipantConverter(member, saveLecture));
        return LectureConverter.lectureDetailConverter(saveLecture);
    }
}
