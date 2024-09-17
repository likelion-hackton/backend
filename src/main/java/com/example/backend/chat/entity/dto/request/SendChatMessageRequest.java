package com.example.backend.chat.entity.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SendChatMessageRequest {

    @NotNull
    private Long chatRoomId;

    // 유저 정보는 auth에서 가져오기

    @NotNull
    private String message;
}
