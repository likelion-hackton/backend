package com.example.backend.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Response
    @ExceptionHandler(value = { ResponseStatusException.class })
    public ResponseEntity<ErrorDetails> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {

        String reason = ex.getReason() != null ? ex.getReason() : "No additional information";

        // 응답 설정
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                ex.getStatusCode().value(),
                reason,
                ex.getMessage(),
                request.getDescription(false),
                "CUSTOM_ERROR_CODE"
        );
        return new ResponseEntity<>(errorDetails, ex.getStatusCode());
    }
}
