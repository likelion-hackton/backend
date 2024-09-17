package com.example.backend.chat.entity.dto.response;

import com.example.backend.chat.entity.dto.MessageInfoDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomInfoResponseDTO {

    @NotNull
    private Long chatRoomId;

    @NotNull
    private String SendMemberInfoImageUrl;

    @NotNull
    private LocalDate created_at;

    private List<MessageInfoDTO> messageList;
}
