package com.example.backend.lecture.controller;

import com.example.backend.lecture.entity.dto.request.CreateLectureRequestDTO;
import com.example.backend.lecture.entity.dto.request.JoinLectureRequestDTO;
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

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LectureDetailResponseDTO> createLecture(@RequestPart("lecture") @Valid CreateLectureRequestDTO req,
                                                                  @RequestPart("images") List<MultipartFile> images,
                                                                  Authentication auth){
        String email = auth.getName();
        return ResponseEntity.ok(lectureService.createLecture(req, email, images));
    }

    @PostMapping("/join")
    public ResponseEntity<LectureDetailResponseDTO> joinLecture(@Valid JoinLectureRequestDTO req, Authentication auth){
        return ResponseEntity.ok(lectureService.joinLecture(req.getLectureId(), auth.getName()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<LectureListResponseDTO>> getAllLecture(){
        return ResponseEntity.ok(lectureService.getAllLecture());
    }
}
