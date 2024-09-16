package com.example.backend.lecture.repository;

import com.example.backend.category.Category;
import com.example.backend.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findByCategory(Category category);
    List<Lecture> findByNameContainingOrDescriptionContaining(String name, String description);
}
