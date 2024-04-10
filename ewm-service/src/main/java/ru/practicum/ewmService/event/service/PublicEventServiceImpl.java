package ru.practicum.ewmService.event.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.dto.SearchParametersPublicRequest;
import ru.practicum.ewmService.event.enums.EventState;
import ru.practicum.ewmService.event.enums.SortOption;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.event.model.QEvent;
import ru.practicum.ewmService.event.service.interfaces.PrivateEventService;
import ru.practicum.ewmService.event.service.interfaces.PublicEventService;
import ru.practicum.ewmService.event.storage.EventRepository;
import ru.practicum.ewmService.exceptions.IncorrectEventStateException;
import ru.practicum.ewmService.exceptions.ObjectNotFoundException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;
    private final PrivateEventService privateEventService;


    @Override
    public List<EventShortDto> getEventsByParameters(SearchParametersPublicRequest searchParametersPublicRequest) {
        List<Event> eventsList = eventRepository.findAll(getPredicate(searchParametersPublicRequest),
                searchParametersPublicRequest.getPageable()).getContent();


        if (searchParametersPublicRequest.getSortOption() == SortOption.EVENT_DATE) {
            return privateEventService.getEventShortDtoListWithStatistic(eventsList).stream()
                    .sorted(Comparator.comparing(EventShortDto::getEventDate)).collect(Collectors.toList());
        }
        return privateEventService.getEventShortDtoListWithStatistic(eventsList).stream()
                .sorted(Comparator.comparingLong(EventShortDto::getViews)).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.info("Event with id {} could not be found", eventId);
            return new ObjectNotFoundException("The required object was not found.", String.format("Event with id=%d was not found", eventId));
        });
        if (event.getState() != EventState.PUBLISHED) {
            log.info("Only published events can be requested, event state = {}", event.getState().name());
            throw new IncorrectEventStateException("Incorrect event State",
                    String.format("Only published events can be requested, event state = %s", event.getState().name()));
        }
        return privateEventService.getEventFullDtoWithStatistic(event);

    }

    private BooleanBuilder getPredicate(SearchParametersPublicRequest searchParametersPublicRequest) {
        QEvent qEvent = QEvent.event;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (searchParametersPublicRequest.getText() != null) {
            booleanBuilder.and(qEvent.description.containsIgnoreCase(searchParametersPublicRequest.getText())
                    .or(qEvent.annotation.containsIgnoreCase(searchParametersPublicRequest.getText())));
        }
        if (searchParametersPublicRequest.getCategories() != null) {
            booleanBuilder.and(qEvent.category.id.in(searchParametersPublicRequest.getCategories()));
        }
        if (searchParametersPublicRequest.getRangeStart() != null) {
            booleanBuilder.and(qEvent.eventDate.after(searchParametersPublicRequest.getRangeStart()));
        }
        if (searchParametersPublicRequest.getRangeEnd() != null) {
            booleanBuilder.and(qEvent.eventDate.before(searchParametersPublicRequest.getRangeEnd()));
        }
        if (searchParametersPublicRequest.getRangeStart() == null && searchParametersPublicRequest.getRangeEnd() == null) {
            booleanBuilder.and(qEvent.eventDate.after(LocalDateTime.now()));
        }
        if (searchParametersPublicRequest.isPaid()) {
            booleanBuilder.and(qEvent.paid.eq(true));
        }
        if (searchParametersPublicRequest.isOnlyAvailable()) {
            booleanBuilder.and(qEvent.paid.eq(true));
        }
        booleanBuilder.and(qEvent.state.eq(EventState.PUBLISHED));


        return booleanBuilder;
    }
}
