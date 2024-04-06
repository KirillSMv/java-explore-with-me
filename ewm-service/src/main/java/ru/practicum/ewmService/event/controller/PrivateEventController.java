package ru.practicum.ewmService.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.dto.NewEventDto;
import ru.practicum.ewmService.event.dto.UpdateEventUserRequest;
import ru.practicum.ewmService.event.service.PrivateEventService;
import ru.practicum.ewmService.request.dto.ParticipationRequestDto;
import ru.practicum.ewmService.request.dto.ParticipationRequestStatusUpdateRequest;
import ru.practicum.ewmService.request.dto.ParticipationRequestStatusUpdateResult;
import ru.practicum.ewmService.user.exception.CustomValidationException;
import ru.practicum.statsClient.StatClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {

    private final PrivateEventService privateEventService;
    private final StatClient statClient;

    @PostMapping
    public ResponseEntity<EventFullDto> addEvent(@PathVariable("userId") Long userId,
                                                 @RequestBody @Validated NewEventDto newEventDto,
                                                 BindingResult errors) {
        checkErrors(errors);
        log.info("addEvent method, pathVar = {}, newEventDto = {}", userId, newEventDto);
        return new ResponseEntity<>(privateEventService.addEventByUser(userId, newEventDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsAddedByUser(@PathVariable @Min(1) Long userId,
                                                                    @RequestParam(value = "from", defaultValue = "0") @Min(value = 0) Integer from,
                                                                    @RequestParam(value = "size", defaultValue = "10") @Min(value = 1) Integer size) {
        log.info("getEventsAddedByUser method, parameters: userId = {}, from = {}, size = {}", userId, from, size);
        return new ResponseEntity<>(privateEventService.getEventsAddedByUser(userId, PageRequest.of(from / size, size)), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getDetailedEventAddedByUser(@PathVariable("userId") @Min(1) Long userId,
                                                                    @PathVariable("eventId") @Min(1) Long eventId,
                                                                    HttpServletRequest request) {
        log.info("getDetailedEventAddedByUser method, parameters userId = {}, eventId = {}", userId, eventId);
        String uri = request.getRequestURI();
        log.info("uri = {}", uri);
        String parameter = request.getContextPath();
        log.info("parameter = {}", parameter);
        String ip = request.getRemoteAddr();
        log.info("ip = {}", ip);
        return new ResponseEntity<>(null, HttpStatus.OK);
        //statClient.postStats()
        //return new ResponseEntity<>(privateEventService.getDetailedEventAddedByUser((userId), eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable("userId") @Min(1) Long userId,
                                                    @PathVariable("eventId") @Min(1) Long eventId,
                                                    @RequestBody @Validated UpdateEventUserRequest updateEventUserRequest,
                                                    BindingResult errors) {
        checkErrors(errors);
        log.info("updateEvent method, parameters userId = {}, eventId = {}, updateEventUserRequest = {}", userId, eventId, updateEventUserRequest);
        return new ResponseEntity<>(privateEventService.updateEvent((userId), eventId, updateEventUserRequest), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getParticipationRequestsFromUser(
            @PathVariable("userId") @Min(1) Long userId,
            @PathVariable("eventId") @Min(1) Long eventId) {
        log.info("getParticipationRequestsFromUser method, parameters userId = {}, eventId = {}", userId, eventId);
        return new ResponseEntity<>(privateEventService.getParticipationRequestsFromUser(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestStatusUpdateResult>> processParticipationRequestsByEventInitiator(
            @PathVariable("userId") @Min(1) Long userId,
            @PathVariable("eventId") @Min(1) Long eventId,
            @RequestBody @Validated ParticipationRequestStatusUpdateRequest participationRequestStatusUpdateRequest,
            BindingResult errors) {
        checkErrors(errors);
        log.info("processParticipationRequestsByEventInitiator method, parameters userId = {}, eventId = {}", userId, eventId);
        return new ResponseEntity<>(privateEventService.processParticipationRequestsByEventInitiator(userId,
                eventId,
                participationRequestStatusUpdateRequest), HttpStatus.OK);
    }

    private void checkErrors(BindingResult errors) {
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    throw new CustomValidationException("Error:" + fieldError.getField() + error.getDefaultMessage(), "Incorrectly made request.");
                }
                throw new CustomValidationException(error.getDefaultMessage(), "Incorrectly made request.");
            }
        }
    }
}
