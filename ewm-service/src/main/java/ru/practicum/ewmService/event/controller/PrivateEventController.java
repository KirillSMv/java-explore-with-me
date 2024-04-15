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
import ru.practicum.ewmService.event.service.interfaces.EventService;
import ru.practicum.ewmService.exceptions.CustomValidationException;
import ru.practicum.ewmService.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewmService.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewmService.request.dto.ParticipationRequestDto;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventFullDto> addEvent(@PathVariable("userId") Long userId,
                                                 @RequestBody @Validated NewEventDto newEventDto,
                                                 BindingResult errors) {
        checkErrors(errors);
        log.info("addEvent method, pathVar = {}, newEventDto = {}", userId, newEventDto);
        return new ResponseEntity<>(eventService.addEventByUser(userId, newEventDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsAddedByUser(@PathVariable @Min(1) Long userId,
                                                                    @RequestParam(value = "from", defaultValue = "0") @Min(value = 0) Integer from,
                                                                    @RequestParam(value = "size", defaultValue = "10") @Min(value = 1) Integer size) {
        log.info("getEventsAddedByUser method, parameters: userId = {}, from = {}, size = {}", userId, from, size);

        return new ResponseEntity<>(eventService.getEventsAddedByUser(userId, PageRequest.of(from / size, size)), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getDetailedEventAddedByUser(@PathVariable("userId") Long userId,
                                                                    @PathVariable("eventId") Long eventId) {
        log.info("getDetailedEventAddedByUser method, parameters userId = {}, eventId = {}", userId, eventId);
        return new ResponseEntity<>(eventService.getDetailedEventAddedByUser((userId), eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEventByInitiator(@PathVariable("userId") Long userId,
                                                               @PathVariable("eventId") Long eventId,
                                                               @RequestBody @Validated UpdateEventUserRequest updateEventUserRequest,
                                                               BindingResult errors) {
        checkErrors(errors);
        log.info("updateEvent method, parameters userId = {}, eventId = {}, updateEventUserRequest = {}", userId, eventId, updateEventUserRequest);
        return new ResponseEntity<>(eventService.updateEvent((userId), eventId, updateEventUserRequest), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getParticipationRequestsForUserEvent(
            @PathVariable("userId") Long userId,
            @PathVariable("eventId") Long eventId) {
        log.info("getParticipationRequestsFromUser method, parameters userId = {}, eventId = {}", userId, eventId);
        return new ResponseEntity<>(eventService.getParticipationRequestsFromUser(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> processParticipationRequestsByEventInitiator(
            @PathVariable("userId") Long userId,
            @PathVariable("eventId") Long eventId,
            @RequestBody @Validated EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
            BindingResult errors) {
        checkErrors(errors);
        log.info("processParticipationRequestsByEventInitiator method, parameters userId = {}, eventId = {}, " +
                "eventRequestStatusUpdateRequest = {}", userId, eventId, eventRequestStatusUpdateRequest);
        return new ResponseEntity<>(eventService.processParticipationRequestsByEventInitiator(userId, eventId,
                eventRequestStatusUpdateRequest), HttpStatus.OK);
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
