package com.example.backend.lecture.service;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.lecture.entity.dto.LectureDtoConverter;
import com.example.backend.lecture.entity.dto.request.CreateLectureRequestDTO;
import com.example.backend.lecture.entity.dto.response.LectureDetailResponseDTO;
import com.example.backend.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@Transactional
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;

    private static final Logger logger = LoggerFactory.getLogger(LectureService.class);

    public LectureDetailResponseDTO createLecture(CreateLectureRequestDTO req){
        Lecture saveLecture = lectureRepository.save(LectureDtoConverter.createLectureConverter(req));
        return LectureDtoConverter.lectureDetailConverter(saveLecture);
    }
}
