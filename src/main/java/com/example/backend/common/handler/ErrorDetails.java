package com.example.backend.common.handler;

import java.util.Date;

public class ErrorDetails {

    private Date timestamp;
    private int statusCode;
    private String error;
    private String message;
    private String details;

    public ErrorDetails(Date timestamp, int statusCode, String error, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.statusCode = statusCode;
        this.error = error;
        this.message = message;
        this.details = details;
    }
}
