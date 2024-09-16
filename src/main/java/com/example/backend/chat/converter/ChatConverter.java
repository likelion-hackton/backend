package com.example.backend.chat.converter;

import com.example.backend.chat.entity.ChatRoom;
import com.example.backend.chat.entity.ChatRoomMember;
import com.example.backend.chat.entity.dto.MessageInfoDTO;
import com.example.backend.chat.entity.dto.response.ChatRoomInfoResponseDTO;
import com.example.backend.common.enums.ChatRoomStatus;
import com.example.backend.lecture.entity.Lecture;
import com.example.backend.member.entity.Member;

import java.util.List;

public class ChatConverter {

    public static ChatRoom createChatRoomConverter(Lecture lecture){
        return ChatRoom.builder()
                .chatRoomName(lecture.getName())
                .chatRoomStatus(ChatRoomStatus.valueOf("ACTIVATE"))
                .created_at(java.time.LocalDateTime.now())
                .lecture(lecture)
                .build();
    }

    public static ChatRoomMember createChatRoomMemberConverter(ChatRoom chatRoom, Member member){
        return ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();
    }

    public static ChatRoomInfoResponseDTO createChatRoomInfoResponseDTOConverter(ChatRoom chatRoom, Member member, List<MessageInfoDTO> messages) {
        ChatRoomInfoResponseDTO chatRoomInfoResponseDTO = new ChatRoomInfoResponseDTO();
        chatRoomInfoResponseDTO.setChatRoomId(chatRoom.getId());
        chatRoomInfoResponseDTO.setSendMemberInfoImageUrl(member.getMemberInfo().getMemberInfoImage() != null
                ? member.getMemberInfo().getMemberInfoImage().getImageUrl() : null);
        chatRoomInfoResponseDTO.setCreated_at(chatRoom.getCreated_at().toLocalDate());
        chatRoomInfoResponseDTO.setMessageList(messages);
        return chatRoomInfoResponseDTO;
    }
}
