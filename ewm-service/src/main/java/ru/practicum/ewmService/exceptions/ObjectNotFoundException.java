package ru.practicum.ewmService.exceptions;

import lombok.Getter;

@Getter
public class ObjectNotFoundException extends RuntimeException {
    String reason;

    public ObjectNotFoundException(String reason, String message) {
        super(message);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
