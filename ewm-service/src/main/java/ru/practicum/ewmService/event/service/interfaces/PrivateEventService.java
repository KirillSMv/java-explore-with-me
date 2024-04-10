package ru.practicum.ewmService.event.service.interfaces;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewmService.event.Event;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.dto.NewEventDto;
import ru.practicum.ewmService.event.dto.UpdateEventUserRequest;
import ru.practicum.ewmService.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewmService.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewmService.request.dto.ParticipationRequestDto;

import java.util.List;
import java.util.Map;

public interface PrivateEventService {
    EventFullDto addEventByUser(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getEventsAddedByUser(Long userId, Pageable pageable);

    EventFullDto getDetailedEventAddedByUser(Long userId, Long eventId);

    EventFullDto getEventFullDtoWithStatistic(Event event);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getParticipationRequestsFromUser(Long userId, Long eventId);

    EventRequestStatusUpdateResult processParticipationRequestsByEventInitiator(Long userId,
                                                                                Long eventId,
                                                                                EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    void updateEventConfirmedRequests(Event event, long value);


    List<EventShortDto> getEventShortDtoListWithStatistic(List<Event> events);

    List<Event> finalAllById(List<Long> eventsIds);

    List<EventFullDto> getEventFullDtoList(List<Event> events);

    Map<Long, Long> getEventIdViewsMap(List<Event> events);
}
