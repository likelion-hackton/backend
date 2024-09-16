package com.example.backend.lecture.controller;

import com.example.backend.category.Category;
import com.example.backend.lecture.entity.dto.request.CreateLectureRequestDTO;
import com.example.backend.lecture.entity.dto.request.CreateOneDayLectureRequestDTO;
import com.example.backend.lecture.entity.dto.request.CreateRegularLectureRequestDTO;
import com.example.backend.lecture.entity.dto.response.LectureDetailResponseDTO;
import com.example.backend.lecture.entity.dto.response.LectureListResponseDTO;
import com.example.backend.lecture.service.LectureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lecture")
public class LectureController {
    private final LectureService lectureService;

    @PostMapping(value = "/create/oneday", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LectureDetailResponseDTO> createOneDayLecture(@RequestPart("lecture") @Valid CreateOneDayLectureRequestDTO req,
                                                                  @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                                                  Authentication auth){
        String email = auth.getName();
        return ResponseEntity.ok(lectureService.createOneDayLecture(req, email, images));
    }

    @PostMapping(value = "/create/regular", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LectureDetailResponseDTO> createRegularLecture(@RequestPart("lecture") @Valid CreateRegularLectureRequestDTO req,
                                                                  @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                                                  Authentication auth){
        String email = auth.getName();
        return ResponseEntity.ok(lectureService.createRegularLecture(req, email, images));
    }

    @GetMapping("/own")
    public ResponseEntity<List<LectureListResponseDTO>> getMyLecture(Authentication auth){
        return ResponseEntity.ok(lectureService.getMyLecture(auth.getName()));
    }

    @PostMapping("/join")
    public ResponseEntity<LectureDetailResponseDTO> joinLecture(@RequestParam("lecture") Long lecture_id, Authentication auth){
        return ResponseEntity.ok(lectureService.joinLecture(lecture_id, auth.getName()));
    }

    @GetMapping("/category")
    public ResponseEntity<List<LectureListResponseDTO>> getLectureByCategory(@RequestParam(defaultValue = "ALL", value = "category") String category){
        return ResponseEntity.ok(lectureService.getLectureByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<LectureListResponseDTO>> searchLectureByKeyword(@RequestParam(value = "keyword") String keyword){
        return ResponseEntity.ok(lectureService.searchLectureByKeyword(keyword));
    }
}
