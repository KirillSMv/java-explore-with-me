package ru.practicum.statsServer.exception;

import java.time.LocalDateTime;

public class CustomValidationException extends RuntimeException {
    private String reason;
    private LocalDateTime timestamp = java.time.LocalDateTime.now();

    public CustomValidationException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}
