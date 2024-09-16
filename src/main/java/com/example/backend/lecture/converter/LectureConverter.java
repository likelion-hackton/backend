package com.example.backend.lecture.converter;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.lecture.entity.OneDayLecture;
import com.example.backend.lecture.entity.RegularLecture;
import com.example.backend.lecture.entity.dto.request.CreateOneDayLectureRequestDTO;
import com.example.backend.lecture.entity.dto.request.CreateRegularLectureRequestDTO;
import com.example.backend.lecture.entity.dto.response.LectureDetailResponseDTO;
import com.example.backend.lecture.entity.dto.response.LectureListResponseDTO;
import com.example.backend.review.entity.Review;

public class LectureConverter {

    public static OneDayLecture createOneDayLectureConverter(CreateOneDayLectureRequestDTO req){
        return OneDayLecture.builder()
                .name(req.getName())
                .description(req.getDescription() != null ? req.getDescription() : "")
                .price(req.getPrice())
                .member_limit(req.getMember_limit())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .date(req.getDate())
                .address(req.getAddress())
                .detailAddress(req.getDetailAddress())
                .category(req.getCategory())
                .build();
    }

    public static RegularLecture createRegularLectureConverter(CreateRegularLectureRequestDTO req){
        return RegularLecture.builder()
                .name(req.getName())
                .description(req.getDescription())
                .price(req.getPrice())
                .member_limit(req.getMember_limit())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .daysOfWeek(req.getDaysOfWeek())
                .address(req.getAddress())
                .detailAddress(req.getDetailAddress())
                .category(req.getCategory())
                .build();
    }

    public static LectureDetailResponseDTO lectureDetailConverter(Lecture lecture){
        LectureDetailResponseDTO dto = new LectureDetailResponseDTO();
        dto.setId(lecture.getId());
        dto.setName(lecture.getName());
        dto.setDescription(lecture.getDescription());
        dto.setPrice(lecture.getPrice());
        dto.setLatitude(lecture.getLatitude());
        dto.setLongitude(lecture.getLongitude());
        dto.setAddress(lecture.getAddress());
        dto.setDetailAddress(lecture.getDetailAddress() != null ? lecture.getDetailAddress() : "");
        dto.setMember_limit(lecture.getMember_limit());
        dto.setStartTime(lecture.getStartTime());
        dto.setEndTime(lecture.getEndTime());
        dto.setCategory(lecture.getCategory());
        if (lecture instanceof OneDayLecture){
            OneDayLecture oneDayLecture = (OneDayLecture) lecture;
            dto.setType("OneDay");
            dto.setDate(oneDayLecture.getDate());
        } else if (lecture instanceof RegularLecture){
            RegularLecture regularLecture = (RegularLecture) lecture;
            dto.setType("Regular");
            dto.setStartDate(regularLecture.getStartDate());
            dto.setEndDate(regularLecture.getEndDate());
            dto.setDaysOfWeek(regularLecture.getDaysOfWeek());
        }
        dto.setImageUrl(lecture.getLectureImages() != null ? lecture.getLectureImages() : null);

        return dto;
    }

    public static LectureListResponseDTO lectureListConverter(Lecture lecture){
        LectureListResponseDTO dto = new LectureListResponseDTO();
        dto.setId(lecture.getId());
        dto.setName(lecture.getName());
        dto.setType(lecture instanceof OneDayLecture ? "OneDay" : "Regular");
        dto.setPrice(lecture.getPrice());
        dto.setSearchCount(lecture.getLectureCount() != null ? lecture.getLectureCount().getViewCount() : 0);
        dto.setImageUrl(lecture.getLectureImages() != null ? lecture.getLectureImages() : null);
        if (lecture.getReviews() != null && lecture.getReviews().size() >= 5) {
            dto.setAverageScore(lecture.getReviews().stream()
                    .mapToLong(Review::getScore)
                    .average()
                    .orElse(0));
        }else {
            dto.setAverageScore(0);
        }
        return dto;
    }
}
