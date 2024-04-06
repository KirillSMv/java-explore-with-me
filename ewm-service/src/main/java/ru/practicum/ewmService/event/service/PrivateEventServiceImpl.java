package ru.practicum.ewmService.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.category.Category;
import ru.practicum.ewmService.category.storage.PublicCategoryRepository;
import ru.practicum.ewmService.event.Event;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.dto.NewEventDto;
import ru.practicum.ewmService.event.dto.UpdateEventUserRequest;
import ru.practicum.ewmService.event.dto.mapper.EventDtoMapper;
import ru.practicum.ewmService.event.enums.EventState;
import ru.practicum.ewmService.event.enums.EventStateAction;
import ru.practicum.ewmService.event.storage.PrivateEventRepository;
import ru.practicum.ewmService.request.dto.ParticipationRequestStatusUpdateRequest;
import ru.practicum.ewmService.request.dto.ParticipationRequestStatusUpdateResult;
import ru.practicum.ewmService.request.dto.ParticipationRequestDto;
import ru.practicum.ewmService.user.User;
import ru.practicum.ewmService.user.exception.EventUpdatingException;
import ru.practicum.ewmService.user.exception.ObjectNotFoundException;
import ru.practicum.ewmService.user.storage.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {

    private final UserRepository userRepository;
    private final EventDtoMapper eventDtoMapper;
    private final PublicCategoryRepository publicCategoryRepository;
    private final PrivateEventRepository privateEventRepository;


    @Override
    @Transactional
    public EventFullDto addEventByUser(Long userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.", String.format("User with id=%d was not found", userId));
        });
        Event event = eventDtoMapper.toEvent(newEventDto);
        Category category = publicCategoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> {
            log.info("Category with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.", String.format("Category with id=%d was not found", newEventDto.getCategory()));
        });
        event.setCategory(category);
        event.setInitiator(user);
        log.info("event = {}", event);
        return eventDtoMapper.toEventFullDto(privateEventRepository.save(event), 0L);
    }

    @Override
    public List<EventShortDto> getEventsAddedByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.", String.format("User with id=%d was not found", userId));
        });
        List<Event> userEvents = privateEventRepository.findAllByInitiator(user, pageable);
        /*
        Нужно теперь получить список views

         */


        return null;
    }

    @Override
    public EventFullDto getDetailedEventAddedByUser(Long userId, Long eventId) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.", String.format("User with id=%d was not found", userId));
        });
        Event event = privateEventRepository.findById(eventId).orElseThrow(() -> {
            log.info("Event with id {} could not be found", eventId);
            return new ObjectNotFoundException("The required object was not found.", String.format("Event with id=%d was not found", eventId));
        });
        if (event.getState() != EventState.PENDING && event.getState() != EventState.CANCELED) {
            log.info("Event with EventState.PUBLISHED cannot be updated");
            throw new EventUpdatingException("For the requested operation the conditions are not met.", "Only pending or canceled events can be changed");
        }
        if (updateEventUserRequest.getStateAction() != null & updateEventUserRequest.getStateAction() != EventStateAction.CANCEL_REVIEW) {
            log.info("User cannot change state of event other than cancel it");
            throw new EventUpdatingException("For the requested operation the conditions are not met.", "User cannot change state of event other than cancel it");
        }
        Event updatedEvent = updateFields(event, updateEventUserRequest);
        return eventDtoMapper.toEventFullDto(updatedEvent, 0L);
    }

    private Event updateFields(Event event, UpdateEventUserRequest updateEventUserRequest) {
        event.setAnnotation(Objects.requireNonNullElse(updateEventUserRequest.getAnnotation(), event.getAnnotation()));
        event.setDescription(Objects.requireNonNullElse(updateEventUserRequest.getDescription(), event.getDescription()));
        event.setEventDate(Objects.requireNonNullElse(updateEventUserRequest.getEventDate(), event.getEventDate()));
        event.setLocation(Objects.requireNonNullElse(updateEventUserRequest.getLocation(), event.getLocation()));
        event.setPaid(Objects.requireNonNullElse(updateEventUserRequest.getPaid(), event.getPaid()));
        event.setParticipantLimit(Objects.requireNonNullElse(updateEventUserRequest.getParticipantLimit(), event.getParticipantLimit()));
        event.setRequestModeration(Objects.requireNonNullElse(updateEventUserRequest.getRequestModeration(), event.getRequestModeration()));
        event.setTitle(Objects.requireNonNullElse(updateEventUserRequest.getTitle(), event.getTitle()));

        if (updateEventUserRequest.getStateAction() != null) {
            event.setState(EventState.CANCELED);
        }

        Long newCategoryId = updateEventUserRequest.getCategory();
        if (newCategoryId != null) {
            if (!event.getCategory().getId().equals(newCategoryId)) {
                Category newCategory = publicCategoryRepository.findById(newCategoryId).orElseThrow(() -> {
                    log.info("Category with id {} could not be found", newCategoryId);
                    return new ObjectNotFoundException("The required object was not found.", String.format("Category with id=%d was not found", newCategoryId));
                });
                event.setCategory(newCategory);
            }
        }
        return event;
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestsFromUser(Long userId, Long eventId) {
        // получить список информации о запросах на участие в событии
        return null;
    }

    @Override
    @Transactional
    public List<ParticipationRequestStatusUpdateResult> processParticipationRequestsByEventInitiator(Long userId, Long eventId, ParticipationRequestStatusUpdateRequest participationRequestStatusUpdateRequest) {
        return null;
    }

    /*
    Extra
    if (updateEventUserRequest.getStateAction() == EventStateAction.CANCEL_REVIEW) {
            if (updateEventUserRequest.getLocation() != null) {
                privateEventRepository.updateEventForNotNullFields(eventId, updateEventUserRequest.getAnnotation(), updateEventUserRequest.getCategory(),
                        updateEventUserRequest.getDescription(), updateEventUserRequest.getEventDate(), updateEventUserRequest.getLocation().getLat(),
                        updateEventUserRequest.getLocation().getLon(), updateEventUserRequest.getPaid(), updateEventUserRequest.getParticipantLimit(),
                        updateEventUserRequest.getRequestModeration(), EventState.CANCELED, updateEventUserRequest.getTitle());
            } else {
                privateEventRepository.updateEventForNotNullFields(eventId, updateEventUserRequest.getAnnotation(), updateEventUserRequest.getCategory(),
                        updateEventUserRequest.getDescription(), updateEventUserRequest.getEventDate(), null,
                        null, updateEventUserRequest.getPaid(), updateEventUserRequest.getParticipantLimit(),
                        updateEventUserRequest.getRequestModeration(), EventState.CANCELED, updateEventUserRequest.getTitle());
            }
        } else {
            if (updateEventUserRequest.getLocation() != null) {
                privateEventRepository.updateEventForNotNullFields(eventId, updateEventUserRequest.getAnnotation(), updateEventUserRequest.getCategory(),
                        updateEventUserRequest.getDescription(), updateEventUserRequest.getEventDate(), updateEventUserRequest.getLocation().getLat(),
                        updateEventUserRequest.getLocation().getLon(), updateEventUserRequest.getPaid(), updateEventUserRequest.getParticipantLimit(),
                        updateEventUserRequest.getRequestModeration(), null, updateEventUserRequest.getTitle());
            } else {
                privateEventRepository.updateEventForNotNullFields(eventId, updateEventUserRequest.getAnnotation(), updateEventUserRequest.getCategory(),
                        updateEventUserRequest.getDescription(), updateEventUserRequest.getEventDate(), null,
                        null, updateEventUserRequest.getPaid(), updateEventUserRequest.getParticipantLimit(),
                        updateEventUserRequest.getRequestModeration(), null, updateEventUserRequest.getTitle());
            }
        }*/
}
