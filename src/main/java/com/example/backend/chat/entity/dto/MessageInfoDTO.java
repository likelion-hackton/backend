package com.example.backend.chat.entity.dto;

import com.example.backend.common.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageInfoDTO {

    @NotNull(message = "메시지가 비어있습니다.")
    private String message;

    @NotBlank(message = "유저 닉네임이 비어있습니다.")
    private String memberNickname;

    private String memberImageUrl;

    @NotNull(message = "메시지 타입이 비어있습니다.")
    private MessageType messageType;
}

