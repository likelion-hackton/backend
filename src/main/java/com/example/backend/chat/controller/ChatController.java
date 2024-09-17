package com.example.backend.chat.controller;

import com.example.backend.chat.entity.dto.MessageInfoDTO;
import com.example.backend.chat.entity.dto.request.CreateChatRoomRequestDTO;
import com.example.backend.chat.entity.dto.request.SendChatMessageRequest;
import com.example.backend.chat.entity.dto.response.ChatRoomInfoResponseDTO;
import com.example.backend.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    // 채팅방 생성
    @PostMapping("/create/chatRoom")
    public ResponseEntity<ChatRoomInfoResponseDTO> createChatRoom(@RequestBody CreateChatRoomRequestDTO request, Authentication auth){
        return ResponseEntity.ok(chatService.createChatRoom(request, auth.getName()));
    }

    // 메세지 보내기
    @PostMapping("/send")
    public ResponseEntity<MessageInfoDTO> sendChatMessage(@RequestBody SendChatMessageRequest request, Authentication auth){
        return ResponseEntity.ok(chatService.sendChatMessage(request, auth.getName()));
    }

}
