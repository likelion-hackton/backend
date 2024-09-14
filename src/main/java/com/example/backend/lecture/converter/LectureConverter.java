package com.example.backend.lecture.converter;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.lecture.entity.OneDayLecture;
import com.example.backend.lecture.entity.RegularLecture;
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
                .dateTime(req.getDateTime())
                .build();
    }

    public static RegularLecture createRegularLectureConverter(CreateRegularLectureRequestDTO req){
        return RegularLecture.builder()
                .name(req.getName())
                .description(req.getDescription())
                .price(req.getPrice())
                .member_limit(req.getMember_limit())
                .location(req.getLocation())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .time(req.getTime())
                .daysOfWeek(req.getDaysOfWeek())
                .build();
    }

    public static LectureDetailResponseDTO lectureDetailConverter(Lecture lecture){
        LectureDetailResponseDTO dto = new LectureDetailResponseDTO();
        dto.setId(lecture.getId());
        dto.setName(lecture.getName());
        dto.setDescription(lecture.getDescription());
        dto.setPrice(lecture.getPrice());
        dto.setLocation(lecture.getLocation());
        dto.setMember_limit(lecture.getMember_limit());
        if (lecture instanceof OneDayLecture){
            OneDayLecture oneDayLecture = (OneDayLecture) lecture;
            dto.setType("OneDay");
            dto.setDateTime(oneDayLecture.getDateTime());
        } else if (lecture instanceof RegularLecture){
            RegularLecture regularLecture = (RegularLecture) lecture;
            dto.setType("Regular");
            dto.setStartDate(regularLecture.getStartDate());
            dto.setEndDate(regularLecture.getEndDate());
            dto.setTime(regularLecture.getTime());
            dto.setDaysOfWeek(regularLecture.getDaysOfWeek());
        }

        return dto;
    }

    public static LectureListResponseDTO lectureListConverter(Lecture lecture){
        LectureListResponseDTO dto = new LectureListResponseDTO();
        dto.setId(lecture.getId());
        dto.setName(lecture.getName());
        dto.setType(lecture instanceof OneDayLecture ? "OneDay" : "Regular");

        if (lecture instanceof OneDayLecture){
            dto.setDateTime(((OneDayLecture) lecture).getDateTime());
        } else if (lecture instanceof RegularLecture){
            dto.
        }

        dto.setImageUrl(lecture.getLectureImages());
        return dto;
    }
}
