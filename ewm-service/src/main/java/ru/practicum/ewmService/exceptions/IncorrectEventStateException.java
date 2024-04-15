package ru.practicum.ewmService.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class IncorrectEventStateException extends RuntimeException {
    private String reason;
    private LocalDateTime timestamp = LocalDateTime.now();

    public IncorrectEventStateException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}
