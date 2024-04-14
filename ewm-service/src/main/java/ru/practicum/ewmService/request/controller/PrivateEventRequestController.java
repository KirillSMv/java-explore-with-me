package ru.practicum.ewmService.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.request.dto.ParticipationRequestDto;
import ru.practicum.ewmService.request.service.interfaces.ParticipationRequestService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateEventRequestController {

    private final ParticipationRequestService participationRequestService;

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> addParticipationRequest(@PathVariable("userId") Long userId,
                                                                           @RequestParam("eventId") Long eventId) {
        log.info("addParticipationRequest method, userId = {}, eventId = {}", userId, eventId);
        return new ResponseEntity<>(participationRequestService.addParticipationRequest(userId, eventId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getOwnParticipationRequests(@PathVariable("userId") Long userId) {
        log.info("getOwnParticipationRequests method, userId = {}", userId);
        return new ResponseEntity<>(participationRequestService.getOwnParticipationRequests(userId), HttpStatus.OK);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelOwnParticipationRequest(@PathVariable("userId") @Min(1) Long userId,
                                                                                 @PathVariable("requestId") @Min(1) Long requestId) {
        log.info("cancelOwnParticipationRequest method, userId = {}, requestId = {}", userId, requestId);
        return new ResponseEntity<>(participationRequestService.cancelOwnParticipationRequest(userId, requestId),
                HttpStatus.OK);
    }
}
