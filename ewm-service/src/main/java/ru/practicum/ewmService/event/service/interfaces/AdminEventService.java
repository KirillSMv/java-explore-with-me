package ru.practicum.ewmService.event.service.interfaces;

import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.dto.SearchParametersAdminRequest;
import ru.practicum.ewmService.event.dto.UpdateEventAdminRequest;

import java.util.List;

public interface AdminEventService {

    EventFullDto updateEventByAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId);


    List<EventFullDto> getEventsBySearch(SearchParametersAdminRequest searchParametersAdminRequest);
}
