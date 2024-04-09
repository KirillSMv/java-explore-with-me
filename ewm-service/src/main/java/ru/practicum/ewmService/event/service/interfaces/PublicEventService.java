package ru.practicum.ewmService.event.service.interfaces;

import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.dto.SearchParametersPublicRequest;

import java.util.List;

public interface PublicEventService {
    List<EventShortDto> getEventsByParameters(SearchParametersPublicRequest searchParametersPublicRequest);

    EventFullDto getEventById(Long eventId);
}
