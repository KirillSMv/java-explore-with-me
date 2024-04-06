package ru.practicum.ewmService.event.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewmService.category.Category;
import ru.practicum.ewmService.category.dto.mapper.CategoryDtoMapper;
import ru.practicum.ewmService.event.Event;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.dto.NewEventDto;
import ru.practicum.ewmService.user.User;
import ru.practicum.ewmService.user.dto.mapper.UserDtoMapper;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventDtoMapper {

    private final CategoryDtoMapper categoryDtoMapper;
    private final UserDtoMapper userDtoMapper;

    public Event toEvent(NewEventDto newEventDto) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        //event.setCategory(newEventDto.getCategory());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setLocation(newEventDto.getLocation());
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.getRequestModeration());
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
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(views);
        eventFullDto.setCategory(categoryDtoMapper.toCategoryDto(event.getCategory()));
        eventFullDto.setInitiator(userDtoMapper.toUserShortDto(event.getInitiator()));
        return eventFullDto;
    }

    public EventShortDto toEventShortDto(Event event, Category category, User initiator, long views) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(views);
        eventShortDto.setCategory(categoryDtoMapper.toCategoryDto(category));
        eventShortDto.setInitiator(userDtoMapper.toUserShortDto(initiator));
        return eventShortDto;
    }

/*    public List<EventShortDto> toEventShortDtoList(List<Event> events, List<Long> views) {



        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        for (Event event : events) {
            eventShortDtoList.add(toEventShortDto(event, ));
        }
    }*/
}
