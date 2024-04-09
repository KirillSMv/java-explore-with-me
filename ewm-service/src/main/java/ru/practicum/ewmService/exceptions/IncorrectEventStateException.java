package ru.practicum.ewmService.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IncorrectEventStateException extends RuntimeException {
    private String reason;
    private LocalDateTime timestamp = LocalDateTime.now();

    public IncorrectEventStateException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}
