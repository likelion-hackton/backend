package com.example.backend.lecture.entity.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinLectureRequestDTO {

    @NotNull(message = "강의 아이디가 비어있습니다.")
    private Long lectureId;
}
