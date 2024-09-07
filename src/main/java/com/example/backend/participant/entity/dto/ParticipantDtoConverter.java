package com.example.backend.participant.entity.dto;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.member.entity.Member;
import com.example.backend.participant.entity.Participant;

public class ParticipantDtoConverter {

    public static Participant createParticipantConverter(Member member, Lecture lecture){
        return Participant.builder()
                .member(member)
                .lecture(lecture)
                .role("CREATOR")
                .build();
    }
}
