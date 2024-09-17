package com.example.backend.chat.repository;

import com.example.backend.chat.entity.ChatRoom;
import com.example.backend.chat.entity.ChatRoomMember;
import com.example.backend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    Boolean existsByMember(Member member);

    Optional<ChatRoomMember> findByChatRoomAndMember(ChatRoom chatRoom, Member member);

    List<ChatRoomMember> findAllByMember(Member member);
}
