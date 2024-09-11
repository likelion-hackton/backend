package com.example.backend.lecture.converter;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.lecture.entity.dto.request.CreateLectureRequestDTO;
import com.example.backend.lecture.entity.dto.response.LectureDetailResponseDTO;
import com.example.backend.lecture.entity.dto.response.LectureListResponseDTO;

public class LectureConverter {

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
        dto.setId(lecture.getId());
        dto.setName(lecture.getName());
        dto.setDescription(lecture.getDescription());
        dto.setPrice(lecture.getPrice());
        dto.setType(lecture.getType());
        dto.setMember_limit(lecture.getMember_limit());
        dto.setDateTime(lecture.getDateTime());
        dto.setLocation(lecture.getLocation());
        return dto;
    }

    public static LectureListResponseDTO lectureListConverter(Lecture lecture){
        LectureListResponseDTO dto = new LectureListResponseDTO();
        dto.setId(lecture.getId());
        dto.setName(lecture.getName());
        dto.setDateTime(lecture.getDateTime());
        dto.setType(lecture.getType());
        dto.setImageUrl(lecture.getLectureImages());
        return dto;
    }
}
