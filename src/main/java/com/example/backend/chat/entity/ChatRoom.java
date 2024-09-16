package com.example.backend.chat.entity;

import com.example.backend.common.enums.ChatRoomStatus;
import com.example.backend.lecture.entity.Lecture;
import com.example.backend.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ChatRoom")
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String chatRoomName; // 채팅방 이름, 채팅방 해당하는 lecture 로 저장

    @NotBlank
    private ChatRoomStatus chatRoomStatus; // 채팅방 타입 - sleep, activate, delete

    @NotBlank
    private LocalDateTime created_at;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;
}
