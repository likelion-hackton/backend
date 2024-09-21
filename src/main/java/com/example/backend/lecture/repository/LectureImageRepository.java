package com.example.backend.lecture.repository;

import com.example.backend.lecture.entity.LectureImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureImageRepository extends JpaRepository<LectureImage, Long> {
}
