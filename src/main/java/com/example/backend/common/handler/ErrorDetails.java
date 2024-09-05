package com.example.backend.common.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class ErrorDetails { // 에러 응답 메시지 구성 정의

    private Date timestamp; // 시간
    private int statusCode; // Status 예시: 400, 404
    private String error; // 에러 텍스트
    private String message; // status + status 이름 + 텍스트, 예시) "404 NOT_FOUND \"존재하지 않는 사용자\"",
    private String details; // 요청한 url
    private String errorCode; // 설정한 error code
}
