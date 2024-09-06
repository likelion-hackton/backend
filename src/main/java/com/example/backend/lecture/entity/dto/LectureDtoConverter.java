package com.example.backend.lecture.entity.dto;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.lecture.entity.dto.request.CreateLectureRequestDTO;
import com.example.backend.lecture.entity.dto.response.LectureDetailResponseDTO;

public class LectureDtoConverter {

    public static Lecture createLectureConverter(CreateLectureRequestDTO req){
        return Lecture.builder()
                .name(req.getName())
                .description(req.getDescription() != null ? req.getDescription() : "") // 설명이 없다면 비어있도록
                .price(req.getPrice())
                .type(req.getType())
                .member_limit(req.getMember_limit())
                .dateTime(req.getDateTime())
                .location(req.getLocation())
                .build();
    }

    public static LectureDetailResponseDTO lectureDetailConverter(Lecture lecture){
        LectureDetailResponseDTO dto = new LectureDetailResponseDTO();
        dto.setName(lecture.getName());
        dto.setDescription(lecture.getDescription());
        dto.setPrice(lecture.getPrice());
        dto.setType(lecture.getType());
        dto.setMember_limit(lecture.getMember_limit());
        dto.setDateTime(lecture.getDateTime());
        dto.setLocation(lecture.getLocation());
        return dto;
    }
}
