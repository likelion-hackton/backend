package com.example.backend.lecture.converter;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.lecture.entity.OneDayLecture;
import com.example.backend.lecture.entity.RegularLecture;
import com.example.backend.lecture.entity.dto.request.CreateLectureRequestDTO;
import com.example.backend.lecture.entity.dto.request.CreateOneDayLectureRequestDTO;
import com.example.backend.lecture.entity.dto.request.CreateRegularLectureRequestDTO;
import com.example.backend.lecture.entity.dto.response.LectureDetailResponseDTO;
import com.example.backend.lecture.entity.dto.response.LectureListResponseDTO;

public class LectureConverter {

    public static OneDayLecture createOneDayLectureConverter(CreateOneDayLectureRequestDTO req){
        return OneDayLecture.builder()
                .name(req.getName())
                .description(req.getDescription() != null ? req.getDescription() : "")
                .price(req.getPrice())
                .member_limit(req.getMember_limit())
                .location(req.getLocation())
                .
    }

    public static RegularLecture createRegularLectureConverter(CreateRegularLectureRequestDTO req){
        return RegularLecture.builder().
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
