package com.example.backend.lecture.service;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.lecture.entity.dto.LectureDtoConverter;
import com.example.backend.lecture.entity.dto.request.CreateLectureRequestDTO;
import com.example.backend.lecture.entity.dto.response.LectureDetailResponseDTO;
import com.example.backend.lecture.repository.LectureRepository;
import com.example.backend.member.entity.Member;
import com.example.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Primary
@Transactional
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;

    private static final Logger logger = LoggerFactory.getLogger(LectureService.class);

    public LectureDetailResponseDTO createLecture(CreateLectureRequestDTO req, String email){
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member == null){
            logger.warn("사용자 찾을 수 없음");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 찾을 수 없음");
        }
        if (member.getPermission().equals("USER")){
            logger.warn("접근 권한 없음");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "접근 권한 없음");
        }
        Lecture saveLecture = lectureRepository.save(LectureDtoConverter.createLectureConverter(req));
        return LectureDtoConverter.lectureDetailConverter(saveLecture);
    }
}
