package com.example.backend.chat.repository;

import com.example.backend.chat.entity.ChatMessage;
import com.example.backend.chat.entity.ChatRoom;
import com.example.backend.chat.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByChatRoomMember(ChatRoomMember chatRoomMember);

    void deleteAllByChatRoomMember(ChatRoomMember chatRoomMember);
}
