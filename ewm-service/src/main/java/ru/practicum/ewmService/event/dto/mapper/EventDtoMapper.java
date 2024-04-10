package ru.practicum.ewmService.event.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewmService.category.dto.mapper.CategoryDtoMapper;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.dto.NewEventDto;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.user.dto.mapper.UserDtoMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventDtoMapper {

    private final CategoryDtoMapper categoryDtoMapper;
    private final UserDtoMapper userDtoMapper;

    public Event toEvent(NewEventDto newEventDto) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setLocation(newEventDto.getLocation());
        event.setPaid(newEventDto.isPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.isRequestModeration());
        event.setTitle(newEventDto.getTitle());
        return event;
    }

    public EventFullDto toEventFullDto(Event event, long views) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setPaid(event.isPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setRequestModeration(event.isRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(views);
        eventFullDto.setCategory(categoryDtoMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setInitiator(userDtoMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setViews(views);
        return eventFullDto;
    }

    public EventShortDto toEventShortDto(Event event, long views) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setPaid(event.isPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(views);
        eventShortDto.setCategory(categoryDtoMapper.toCategoryDto(event.getCategory()));
        eventShortDto.setInitiator(userDtoMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setViews(views);
        return eventShortDto;
    }

    public List<EventShortDto> toEventShortDtoList(List<Event> events, Map<Long, Long> views) {
        return events.stream().map(event -> toEventShortDto(event, views.get(event.getId()))).collect(Collectors.toList());
    }

    public List<EventFullDto> toEventFullDtoList(List<Event> events, Map<Long, Long> views) {
        return events.stream().map(event -> toEventFullDto(event, views.get(event.getId()))).collect(Collectors.toList());
    }
}
