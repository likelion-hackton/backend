package com.example.backend.participant.repository;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.member.entity.Member;
import com.example.backend.participant.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    long countByLecture(Lecture lecture);
    boolean existsByLectureAndMember(Lecture lecture, Member member);

    @Query("SELECT DISTINCT p.lecture FROM Participant p WHERE p.member.id = :memberId")
    List<Lecture> findLecturesByMemberId(@Param("memberId") Long memberId);
}
