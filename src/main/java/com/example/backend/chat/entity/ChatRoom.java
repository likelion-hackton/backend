package com.example.backend.chat.entity;

import com.example.backend.common.enums.ChatRoomStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    private ChatRoomStatus chatRoomType; // 채팅방 타입 - sleep, activate, delete

    @NotBlank
    private LocalDateTime created_at;
}
