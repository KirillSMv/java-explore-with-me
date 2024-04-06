package ru.practicum.ewmService.user.exception;

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
