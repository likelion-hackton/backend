package com.example.backend.participant.repository;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.participant.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    long countByLecture(Lecture lecture);
}
