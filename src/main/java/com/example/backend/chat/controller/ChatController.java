package com.example.backend.chat.controller;

import com.example.backend.chat.entity.dto.MessageInfoDTO;
import com.example.backend.chat.entity.dto.request.CreateChatRoomRequestDTO;
import com.example.backend.chat.entity.dto.request.SendChatMessageRequest;
import com.example.backend.chat.entity.dto.response.ChatRoomAllResponseDTO;
import com.example.backend.chat.entity.dto.response.ChatRoomInfoResponseDTO;
import com.example.backend.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    // 채팅방 전체 조회
    @GetMapping("/chatRoom/all")
    public ResponseEntity<ChatRoomAllResponseDTO> getChatRoomAll(Authentication auth){
        return ResponseEntity.ok(chatService.getChatRoomAll(auth.getName()));
    }
}
