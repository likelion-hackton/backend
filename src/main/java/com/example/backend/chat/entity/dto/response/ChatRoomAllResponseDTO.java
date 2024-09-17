package com.example.backend.chat.entity.dto.response;

import com.example.backend.chat.entity.dto.ChatRoomInfoDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomAllResponseDTO {

    // 현재 유저  imageUrl
    private String MemberInfoImageUrl;

    List<ChatRoomInfoDTO> chatRoomList;
}
