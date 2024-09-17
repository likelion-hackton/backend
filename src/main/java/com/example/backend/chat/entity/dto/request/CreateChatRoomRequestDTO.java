package com.example.backend.chat.entity.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateChatRoomRequestDTO {
    // member -> auth 에서 가져오기

    @NotNull
    private Long lectureId;
}
