package com.example.backend.chat.service;

import com.example.backend.chat.converter.ChatConverter;
import com.example.backend.chat.entity.ChatMessage;
import com.example.backend.chat.entity.ChatRoom;
import com.example.backend.chat.entity.ChatRoomMember;
import com.example.backend.chat.entity.dto.MessageInfoDTO;
import com.example.backend.chat.entity.dto.request.CreateChatRoomRequestDTO;
import com.example.backend.chat.entity.dto.request.SendChatMessageRequest;
import com.example.backend.chat.entity.dto.response.ChatRoomInfoResponseDTO;
import com.example.backend.chat.repository.ChatMessageRepository;
import com.example.backend.chat.repository.ChatRoomMemberRepository;
import com.example.backend.chat.repository.ChatRoomRepository;
import com.example.backend.lecture.entity.Lecture;
import com.example.backend.lecture.repository.LectureRepository;
import com.example.backend.member.entity.Member;
import com.example.backend.member.repository.MemberRepository;
import com.example.backend.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final ParticipantRepository participantRepository;

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final  ChatMessageRepository chatMessageRepository;

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    // 채팅방 생성
    @Transactional
    public ChatRoomInfoResponseDTO createChatRoom(CreateChatRoomRequestDTO request, String memberEmail){
        Long lectureId = request.getLectureId();

        // 존재하는 유저 인지 확인
        Member member = memberRepository.findByEmail(memberEmail).orElse(null);
        if (member == null) {
            logger.warn("존재하지 않는 회원입니다.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.");
        }

        // 존재하는 강의인지 확인
        Lecture lecture = lectureRepository.findById(lectureId).orElse(null);
        if (lecture == null) {
            logger.warn("존재하지 않는 강의입니다.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 강의입니다.");
        }

        // 이미 채팅방이 존재하는지 확인
        // 먼저 chatRoom 이 존재하는지 확인 && chatRoomMember 에 해당 유저가 존재하는지 확인
        if (chatRoomRepository.existsByLecture(lecture)
                && chatRoomMemberRepository.existsByMember(member)) {
            logger.warn("이미 채팅방이 존재합니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 채팅방이 존재합니다.");
        }

        // 채팅방 생성
        ChatRoom chatRoom = ChatConverter.createChatRoomConverter(lecture);
        chatRoomRepository.save(chatRoom);

        // 채팅방 룸 멤버 생성(문의 한 참여자)
        ChatRoomMember chatRoomMember = ChatConverter.createChatRoomMemberConverter(chatRoom,member);
        chatRoomMember.setIsLectureOwner(false);
        chatRoomMemberRepository.save(chatRoomMember);

        // 채팅방 룸 멤버 생성(클래스 주최자 총 2명 추가)
        Long lectureOwnerId = participantRepository.findMemberIdByLectureId(lectureId);
        Member lectureOwner = memberRepository.findById(lectureOwnerId).orElse(null);
        if (lectureOwner == null) {
            logger.warn("강의 주최자가 존재하지 않습니다.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "강의 주최자가 존재하지 않습니다.");
        }
        ChatRoomMember chatRoomMemberOwner = ChatConverter.createChatRoomMemberConverter(chatRoom,lectureOwner);
        chatRoomMemberOwner.setIsLectureOwner(true);
        chatRoomMemberRepository.save(chatRoomMemberOwner);

        // ChatRoomInfoResponseDTO 생성
        return ChatConverter.createChatRoomInfoResponseDTOConverter(chatRoom, member, null);
    }

    // 메세지 생성
    @Transactional
    public MessageInfoDTO sendChatMessage(SendChatMessageRequest request, String memberEmail){
        // 존재하는 유저 인지 확인
        Member member = memberRepository.findByEmail(memberEmail).orElse(null);
        if (member == null) {
            logger.warn("존재하지 않는 회원입니다.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.");
        }

        // 존재하는 채팅방인지 확인
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId()).orElse(null);
        if (chatRoom == null) {
            logger.warn("존재하지 않는 채팅방입니다.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 채팅방입니다.");
        }

        // 채팅방에 속한 멤버인지 확인
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomAndMember(chatRoom, member).orElse(null);
        if (chatRoomMember == null) {
            logger.warn("채팅방에 속한 멤버가 아닙니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "채팅방에 속한 멤버가 아닙니다.");
        }

        // 메세지 저장
        ChatMessage chatMessage = ChatConverter.createChatMessageConverter(request.getMessage(), chatRoomMember);
        chatMessageRepository.save(chatMessage);

        // MessageInfoDTO 생성
        return ChatConverter.createMessageInfoDTOConverter(chatMessage, chatRoomMember);
    }
}