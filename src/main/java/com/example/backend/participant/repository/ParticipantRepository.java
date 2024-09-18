package com.example.backend.participant.repository;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.member.entity.Member;
import com.example.backend.participant.entity.Participant;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    long countByLecture(Lecture lecture);
    boolean existsByLectureAndMemberAndRole(Lecture lecture, Member member, String role);


    @Query("SELECT p.lecture FROM Participant p WHERE p.member.id = :member_id AND p.role = :role")
    List<Lecture> findLecturesByMemberIdAndRole(@Param("member_id") Long member_id, @Param("role") String role);

    @Query("SELECT COUNT(p) FROM Participant p WHERE p.lecture.id = :lectureId")
    long findMemberIdByLectureId(@Param("lectureId") Long lectureId);

    @Query("SELECT DISTINCT p.lecture FROM Participant p WHERE p.member.id = :memberId")
    List<Lecture> findLecturesByMemberId(@Param("memberId") Long memberId);

}
