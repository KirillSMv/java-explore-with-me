package ru.practicum.ewmService.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryProcessingException extends RuntimeException {

    private String reason;
    private LocalDateTime timestamp = java.time.LocalDateTime.now();

    public CategoryProcessingException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}
