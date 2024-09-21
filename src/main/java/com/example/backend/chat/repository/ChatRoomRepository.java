package com.example.backend.chat.repository;

import com.example.backend.chat.entity.ChatRoom;
import com.example.backend.chat.entity.ChatRoomMember;
import com.example.backend.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Boolean existsByLectureAndAndChatRoomMember(Lecture lecture, ChatRoomMember chatRoomMember);
}
