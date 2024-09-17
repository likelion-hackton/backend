package com.example.backend.chat.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomInfoDTO {

    private String receiverImageUrl;

    @NotBlank
    private String chatRoomName;

    private Long notReadMessageCount;

    private String receiverNickName;

    @NotNull
    private Boolean isLectureOwner;
}
