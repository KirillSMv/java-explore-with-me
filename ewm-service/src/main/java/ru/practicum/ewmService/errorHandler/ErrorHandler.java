package ru.practicum.ewmService.errorHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewmService.exceptions.*;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static ru.practicum.ewmService.Constants.TIME_PATTERN;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final IllegalArgumentException e) {
        log.error("Exception was thrown: {}", e.getMessage());
        return new ErrorResponse("BAD_REQUEST", "Incorrectly made request.", e.getMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final SQLException e) {
        log.error("Exception was thrown: {}", e.getMessage());
        return new ErrorResponse("CONFLICT", "Integrity constraint has been violated.", e.getMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final CategoryProcessingException e) {
        log.error("Exception was thrown: {}", e.getMessage());
        return new ErrorResponse("CONFLICT", e.getReason(), e.getMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final ObjectNotFoundException e) {
        log.error("Exception was thrown: {}", e.getMessage());
        return new ErrorResponse("NOT_FOUND", e.getReason(), e.getMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }

/*    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //todo print HttpStatus.BAD_REQUEST
    public ErrorResponse handleMissingParams(final MissingServletRequestParameterException e) {
        log.error("Exception was thrown: {}", e.getMessage());
        return new ErrorResponse("BAD_REQUEST", String.format("Required request parameter %s is missing", e.getParameterName()), e.getMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }*/

    //javax validation exception
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final MethodArgumentNotValidException e) {
        log.error("Validation exception: {}", e.getMessage());
        return new ErrorResponse("BAD_REQUEST", "Incorrectly made request.", e.getMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }

    //EventDate not valid exception
    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN) //HttpStatus.FORBIDDEN.toString()
    public ErrorResponse handle(final InvalidEventDateException e) {
        log.error("InvalidEventDateException: {}", e.getMessage());
        return new ErrorResponse("FORBIDDEN", e.getReason(), e.getMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) //HttpStatus.FORBIDDEN.toString()
    public ErrorResponse handle(final CustomValidationException e) {
        log.error("CustomValidationException: {}", e.getMessage());
        return new ErrorResponse("BAD_REQUEST", e.getReason(), e.getMessage(), e.getTimestamp().format(TIME_PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handle(final EventUpdatingException e) {
        log.error("EventUpdatingException: {}", e.getMessage());
        return new ErrorResponse("FORBIDDEN", e.getReason(), e.getMessage(), e.getTimestamp().format(TIME_PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final ParticipationRequestProcessingException e) {
        log.error("ParticipationRequestProcessingException: {}", e.getMessage());
        return new ErrorResponse("CONFLICT", e.getReason(), e.getMessage(), e.getTimestamp().format(TIME_PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handle(final EventAdminUpdateException e) {
        log.error("EventAdminUpdateException: {}", e.getMessage());
        return new ErrorResponse("FORBIDDEN", e.getReason(), e.getMessage(), e.getTimestamp().format(TIME_PATTERN));
    }

/*    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(final Throwable e) {
        log.error("Throwable:", e);
        return new ErrorResponse("FORBIDDEN", "Internal error", e.getMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }*/

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final MissingPathVariableException e) {
        log.error("MissingPathVariableException:", e);
        return new ErrorResponse(e.getStackTrace().toString(), e.getLocalizedMessage(), e.getMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException:", e);
        return new ErrorResponse(e.getStackTrace().toString(), e.getLocalizedMessage(), e.getMessage(), LocalDateTime.now().format(TIME_PATTERN));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handle(final IncorrectEventStateException e) {
        log.error("EventAdminUpdateException: {}", e.getMessage());
        return new ErrorResponse("FORBIDDEN", e.getReason(), e.getMessage(), e.getTimestamp().format(TIME_PATTERN));
    }

}




