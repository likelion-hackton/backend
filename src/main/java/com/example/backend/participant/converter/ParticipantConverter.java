package com.example.backend.participant.converter;

import com.example.backend.lecture.entity.Lecture;
import com.example.backend.member.entity.Member;
import com.example.backend.participant.entity.Participant;

public class ParticipantConverter {

    // 강의를 생성할 때 적용
    public static Participant createParticipantConverter(Member member, Lecture lecture){
        return Participant.builder()
                .member(member)
                .lecture(lecture)
                .role("CREATOR")
                .build();
    }

    public static Participant joinParticipantConverter(Member member, Lecture lecture, Long count){
        return Participant.builder()
                .member(member)
                .lecture(lecture)
                .role("USER")
                .memberCount(count)
                .build();
    }
}
