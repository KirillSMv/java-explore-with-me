package ru.practicum.ewmService.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomValidationException extends RuntimeException {
    private String reason;
    private LocalDateTime timestamp = java.time.LocalDateTime.now();

    public CustomValidationException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}


