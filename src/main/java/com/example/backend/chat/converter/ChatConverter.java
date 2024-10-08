package com.example.backend.chat.converter;

import com.example.backend.chat.entity.ChatMessage;
import com.example.backend.chat.entity.ChatRoom;
import com.example.backend.chat.entity.ChatRoomMember;
import com.example.backend.chat.entity.dto.ChatRoomInfoDTO;
import com.example.backend.chat.entity.dto.MessageInfoDTO;
import com.example.backend.chat.entity.dto.response.ChatRoomAllResponseDTO;
import com.example.backend.chat.entity.dto.response.ChatRoomInfoResponseDTO;
import com.example.backend.common.enums.ChatRoomStatus;
import com.example.backend.common.enums.MessageType;
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

    public static ChatMessage createChatMessageConverter(String message, ChatRoomMember chatRoomMember){
        return ChatMessage.builder()
                .message(message)
                .created_at(java.time.LocalDateTime.now())
                .isRead(false)
                .chatRoomMember(chatRoomMember)
                .build();
    }

    public static MessageInfoDTO createMessageInfoDTOConverter(ChatMessage chatMessage, ChatRoomMember chatRoomMember, Member member){
        MessageInfoDTO messageInfoDTO = new MessageInfoDTO();
        messageInfoDTO.setMessage(chatMessage.getMessage());
        messageInfoDTO.setMemberNickname(chatRoomMember.getMember().getMemberInfo().getNickname());
        messageInfoDTO.setMemberImageUrl(chatRoomMember.getMember().getMemberInfo().getMemberInfoImage() != null
                ? chatRoomMember.getMember().getMemberInfo().getMemberInfoImage().getImageUrl() : null);
        messageInfoDTO.setMessageType(chatRoomMember.getMember() == member ? MessageType.SENDER : MessageType.RECEIVER);

        return messageInfoDTO;
    }

    public static ChatRoomInfoDTO createChatRoomInfoDTOConverter(ChatRoomMember senderChatRoomMember, ChatRoomMember recevierChatRoomMember,List<ChatMessage> chatMessageList) {
        ChatRoomInfoDTO chatRoomInfoDTO = new ChatRoomInfoDTO();

        // 내가 보내지 않은 메세지 중에 읽지 않은 메세지 카운트
        Long notReadMessageCount = chatMessageList.stream()
                .filter(chatMessage -> chatMessage.getChatRoomMember().getMember() != senderChatRoomMember.getMember())
                .filter(chatMessage -> !chatMessage.getIsRead())
                .count();

        chatRoomInfoDTO.setReceiverImageUrl(recevierChatRoomMember.getMember().getMemberInfo().getMemberInfoImage() != null
                ? recevierChatRoomMember.getMember().getMemberInfo().getMemberInfoImage().getImageUrl() : null);
        chatRoomInfoDTO.setReceiverNickName(recevierChatRoomMember.getIsLectureOwner() ? recevierChatRoomMember.getMember().getMemberInfo().getNickname() : null);
        chatRoomInfoDTO.setChatRoomName(senderChatRoomMember.getChatRoom().getChatRoomName());
        chatRoomInfoDTO.setNotReadMessageCount(notReadMessageCount);
        chatRoomInfoDTO.setIsLectureOwner(senderChatRoomMember.getIsLectureOwner());
        chatRoomInfoDTO.setChatRoomId(senderChatRoomMember.getChatRoom().getId());
        return chatRoomInfoDTO;
    }

    public static ChatRoomAllResponseDTO createChatRoomAllResponseDTOConverter(Member member, List<ChatRoomInfoDTO> chatRoomInfoDTOList) {
        ChatRoomAllResponseDTO chatRoomAllResponseDTO = new ChatRoomAllResponseDTO();
        chatRoomAllResponseDTO.setMemberInfoImageUrl(member.getMemberInfo().getMemberInfoImage() != null
                ? member.getMemberInfo().getMemberInfoImage().getImageUrl() : null);
        chatRoomAllResponseDTO.setChatRoomList(chatRoomInfoDTOList);
        return chatRoomAllResponseDTO;

    }
}
