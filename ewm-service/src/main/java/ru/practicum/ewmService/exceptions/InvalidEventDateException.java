package ru.practicum.ewmService.exceptions;

import lombok.Getter;

@Getter
public class InvalidEventDateException extends RuntimeException {
    String reason = "For the requested operation the conditions are not met.";

    public InvalidEventDateException(String message) {
        super(message);
    }
}
