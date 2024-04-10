package ru.practicum.ewmService.event.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.category.model.Category;
import ru.practicum.ewmService.category.storage.CategoryRepository;
import ru.practicum.ewmService.event.QEvent;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.SearchParametersAdminRequest;
import ru.practicum.ewmService.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewmService.event.enums.EventState;
import ru.practicum.ewmService.event.enums.EventStateAction;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.event.service.interfaces.AdminEventService;
import ru.practicum.ewmService.event.service.interfaces.PrivateEventService;
import ru.practicum.ewmService.event.storage.EventRepository;
import ru.practicum.ewmService.exceptions.EventAdminUpdateException;
import ru.practicum.ewmService.exceptions.ObjectNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final PrivateEventService privateEventService;
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public EventFullDto updateEventByAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.info("Event with id {} could not be found", eventId);
            return new ObjectNotFoundException("The required object was not found.", String.format("Event with id=%d was not found", eventId));
        });
        checkUpdateRequirements(event, updateEventAdminRequest);
        Event updatedEvent = eventRepository.save(updateEventFields(event, updateEventAdminRequest));
        return privateEventService.getEventFullDtoWithStatistic(updatedEvent);
    }

    @Override
    public List<EventFullDto> getEventsBySearch(SearchParametersAdminRequest searchParametersAdminRequest) {
        List<Event> eventsList = eventRepository.findAll(getPredicate(searchParametersAdminRequest), searchParametersAdminRequest.getPageable()).getContent();
        return privateEventService.getEventFullDtoList(eventsList);
    }

    private BooleanBuilder getPredicate(SearchParametersAdminRequest searchParametersAdminRequest) {
        QEvent qEvent = QEvent.event;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (searchParametersAdminRequest.getUsers() != null) {
            booleanBuilder.and(qEvent.initiator.id.in(searchParametersAdminRequest.getUsers()));
        }
        if (searchParametersAdminRequest.getStates() != null) {
            booleanBuilder.and(qEvent.state.in(searchParametersAdminRequest.getStates()));
        }
        if (searchParametersAdminRequest.getCategories() != null) {
            booleanBuilder.and(qEvent.category.id.in(searchParametersAdminRequest.getCategories()));
        }
        if (searchParametersAdminRequest.getRangeStart() != null) {
            booleanBuilder.and(qEvent.eventDate.after(searchParametersAdminRequest.getRangeStart()));
        }
        if (searchParametersAdminRequest.getRangeEnd() != null) {
            booleanBuilder.and(qEvent.eventDate.before(searchParametersAdminRequest.getRangeEnd()));
        }
        return booleanBuilder;
    }

    private void checkUpdateRequirements(Event event, UpdateEventAdminRequest updateEventAdminRequest) {
        if (updateEventAdminRequest.getStateAction() == EventStateAction.PUBLISH_EVENT) {
            if (event.getState() != EventState.PENDING) {
                throw new EventAdminUpdateException("For the requested operation the conditions are not met.",
                        String.format("Cannot publish the event because it's not in the right state: %s", event.getState().toString()));
            }
            if (LocalDateTime.now().plusHours(1).isAfter(event.getEventDate())) {
                throw new EventAdminUpdateException("For the requested operation the conditions are not met.",
                        ("Event cannot be published due to its start in less than 1 hour"));
            }
        } else if (updateEventAdminRequest.getStateAction() == EventStateAction.REJECT_EVENT) {
            if (event.getState() != EventState.PENDING) {
                throw new EventAdminUpdateException("For the requested operation the conditions are not met.",
                        String.format("Cannot reject the event because it's not in the right state: %s", event.getState().toString()));
            }
        }
    }

    private Event updateEventFields(Event event, UpdateEventAdminRequest updateEventAdminRequest) {
        event.setAnnotation(Objects.requireNonNullElse(updateEventAdminRequest.getAnnotation(), event.getAnnotation()));
        event.setDescription(Objects.requireNonNullElse(updateEventAdminRequest.getDescription(), event.getDescription()));
        event.setEventDate(Objects.requireNonNullElse(updateEventAdminRequest.getEventDate(), event.getEventDate()));
        event.setLocation(Objects.requireNonNullElse(updateEventAdminRequest.getLocation(), event.getLocation()));
        event.setPaid(Objects.requireNonNullElse(updateEventAdminRequest.getPaid(), event.isPaid()));
        event.setParticipantLimit(Objects.requireNonNullElse(updateEventAdminRequest.getParticipantLimit(), event.getParticipantLimit()));
        event.setRequestModeration(Objects.requireNonNullElse(updateEventAdminRequest.getRequestModeration(), event.isRequestModeration()));
        event.setTitle(Objects.requireNonNullElse(updateEventAdminRequest.getTitle(), event.getTitle()));
        if (updateEventAdminRequest.getStateAction() == EventStateAction.PUBLISH_EVENT) {
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else if (updateEventAdminRequest.getStateAction() == EventStateAction.REJECT_EVENT) {
            event.setState(EventState.CANCELED);
        }

        Long newCategoryId = updateEventAdminRequest.getCategory();
        if (newCategoryId != null) {
            if (!event.getCategory().getId().equals(newCategoryId)) {
                Category newCategory = categoryRepository.findById(newCategoryId).orElseThrow(() -> {
                    log.info("Category with id {} could not be found", newCategoryId);
                    return new ObjectNotFoundException("The required object was not found.", String.format("Category with id=%d was not found", newCategoryId));
                });
                event.setCategory(newCategory);
            }
        }
        return event;
    }
}
