package ru.practicum.ewmService.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.dto.NewEventDto;
import ru.practicum.ewmService.event.dto.UpdateEventUserRequest;
import ru.practicum.ewmService.request.dto.ParticipationRequestStatusUpdateRequest;
import ru.practicum.ewmService.request.dto.ParticipationRequestStatusUpdateResult;
import ru.practicum.ewmService.request.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateEventService {
    EventFullDto addEventByUser(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getEventsAddedByUser(Long userId, Pageable pageable);

    EventFullDto getDetailedEventAddedByUser(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getParticipationRequestsFromUser(Long userId, Long eventId);

    List<ParticipationRequestStatusUpdateResult> processParticipationRequestsByEventInitiator(Long userId,
                                                                                              Long eventId,
                                                                                              ParticipationRequestStatusUpdateRequest participationRequestStatusUpdateRequest);
}
