package com.example.backend.lecture.repository;

import com.example.backend.category.Category;
import com.example.backend.lecture.entity.Lecture;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findByCategory(Category category);

    // 대소문자 구별 없이 이름 또는 설명에 키워드가 포함된 강의 조회
    @Query("SELECT l FROM Lecture l WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(l.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Lecture> findByNameContainingOrDescriptionContaining(@Param("keyword") String keyword);

    // 조회된 강의 조회 1증가
    @Modifying(clearAutomatically = true)
    @Query("UPDATE LectureCount lc SET lc.viewCount = lc.viewCount + 1 WHERE lc.lecture.id = :lectureId")
    void incrementViewCount(@Param("lectureId") Long lectureId);

    // 범위만큼 검색이 가장 많이 된 강의 조회
    @Query("SELECT l FROM Lecture l JOIN l.lectureCount lc ORDER BY lc.viewCount DESC")
    List<Lecture> findTop3ByOrderByViewCountDesc(Pageable pageable);
}
