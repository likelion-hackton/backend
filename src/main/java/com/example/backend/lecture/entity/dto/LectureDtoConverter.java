package com.example.backend.lecture.entity.dto;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.lecture.entity.dto.request.CreateLectureRequestDTO;

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
}
