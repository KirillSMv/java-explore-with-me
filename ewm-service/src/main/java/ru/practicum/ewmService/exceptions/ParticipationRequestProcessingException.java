package ru.practicum.ewmService.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ParticipationRequestProcessingException extends RuntimeException {
    private String reason;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ParticipationRequestProcessingException(String reason, String message) {
        super(message);
        this.reason = reason;
    }
}
