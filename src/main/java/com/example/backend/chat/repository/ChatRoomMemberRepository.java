package com.example.backend.chat.repository;

import com.example.backend.chat.entity.ChatRoomMember;
import com.example.backend.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    Boolean existsByMember(Member member);
}
