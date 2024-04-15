package ru.practicum.ewmService.event.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.category.model.Category;
import ru.practicum.ewmService.category.storage.CategoryRepository;
import ru.practicum.ewmService.event.dto.*;
import ru.practicum.ewmService.event.dto.mapper.EventDtoMapper;
import ru.practicum.ewmService.event.enums.EventState;
import ru.practicum.ewmService.event.enums.EventStateAction;
import ru.practicum.ewmService.event.enums.SortOption;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.event.model.QEvent;
import ru.practicum.ewmService.event.service.interfaces.EventService;
import ru.practicum.ewmService.event.service.interfaces.StatsRecordingService;
import ru.practicum.ewmService.event.storage.EventRepository;
import ru.practicum.ewmService.exceptions.*;
import ru.practicum.ewmService.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewmService.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewmService.request.dto.ParticipationRequestDto;
import ru.practicum.ewmService.request.dto.mapper.ParticipationRequestMapper;
import ru.practicum.ewmService.request.enums.RequestState;
import ru.practicum.ewmService.request.model.ParticipationRequest;
import ru.practicum.ewmService.request.storage.ParticipationRequestRepository;
import ru.practicum.ewmService.user.model.User;
import ru.practicum.ewmService.user.storage.UserRepository;
import ru.practicum.statsDto.StatsParamsDto;
import ru.practicum.statsDto.StatsToUserDto;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventDtoMapper eventDtoMapper;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final StatsRecordingService statsRecordingService;
    private final ParticipationRequestRepository participationRequestRepository;
    private final ParticipationRequestMapper participationRequestMapper;

    @Override
    @Transactional
    public EventFullDto addEventByUser(Long userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.", String.format("User with id=%d was not found", userId));
        });
        Event event = eventDtoMapper.toEvent(newEventDto);
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> {
            log.error("Category with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.", String.format("Category with id=%d was not found", newEventDto.getCategory()));
        });
        event.setCategory(category);
        event.setInitiator(user);
        log.debug("event = {}", event);
        return eventDtoMapper.toEventFullDto(eventRepository.save(event), 0L);
    }

    @Override
    public List<EventShortDto> getEventsAddedByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.", String.format("User with id=%d was not found", userId));
        });
        List<Event> events = eventRepository.findAllByInitiator(user, pageable);

        if (events.isEmpty()) {
            return new ArrayList<>();
        }
        return getEventShortDtoListWithStatistic(events);
    }

    @Override
    public EventFullDto getDetailedEventAddedByUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.", String.format("User with id=%d was not found", userId));
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} could not be found", eventId);
            return new ObjectNotFoundException("The required object was not found.", String.format("Event with id=%d was not found", eventId));
        });
        return getEventFullDtoWithStatistic(event);
    }

    @Override
    public EventFullDto getEventFullDtoWithStatistic(Event event) {
        if (event.getState() != EventState.PUBLISHED) {
            return eventDtoMapper.toEventFullDto(event, 0L);
        }
        List<StatsToUserDto> statsList = makeClientRequest(event);
        log.debug("statsList size = {}", statsList.size());
        if (statsList.isEmpty()) {
            return eventDtoMapper.toEventFullDto(event, 0L);
        }
        return eventDtoMapper.toEventFullDto(event, statsList.get(0).getHits());
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.", String.format("User with id=%d was not found", userId));
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} could not be found", eventId);
            return new ObjectNotFoundException("The required object was not found.", String.format("Event with id=%d was not found", eventId));
        });
        checkExceptionalCasesWhenUpdateEvent(event, updateEventUserRequest);
        Event updatedEvent = updateEventFields(event, updateEventUserRequest);
        return eventDtoMapper.toEventFullDto(updatedEvent, 0L);
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestsFromUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.", String.format("User with id=%d was not found", userId));
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} could not be found", eventId);
            return new ObjectNotFoundException("The required object was not found.", String.format("Event with id=%d was not found", eventId));
        });
        List<ParticipationRequest> participationRequestList = participationRequestRepository.findAllByEvent(event);
        return participationRequestMapper.toParticipationRequestDtoList(participationRequestList);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult processParticipationRequestsByEventInitiator(Long userId, Long eventId,
                                                                                       EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.", String.format("User with id=%d was not found", userId));
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} could not be found", eventId);
            return new ObjectNotFoundException("The required object was not found.", String.format("Event with id=%d was not found", eventId));
        });
        checkExceptionalCasesWhenProcessRequests(event);

        List<ParticipationRequest> participationRequestList = participationRequestRepository.findAllById(eventRequestStatusUpdateRequest.getRequestIds());
        boolean isStatusPending = participationRequestList.stream().allMatch(participationRequest -> participationRequest.getStatus() == RequestState.PENDING);
        if (!isStatusPending) {
            log.error("Not all participation requests have status Pending");
            throw new CustomValidationException("Incorrectly made request.", "Request must have status PENDING");
        }

        List<ParticipationRequest> confirmedParticipationRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedParticipationRequests = new ArrayList<>();
        if (eventRequestStatusUpdateRequest.getStatus() == RequestState.CONFIRMED) {
            if (event.getConfirmedRequests() + participationRequestList.size() > event.getParticipantLimit()) {
                int oversizeForRequests = (int) (event.getParticipantLimit() - event.getConfirmedRequests() + participationRequestList.size());
                confirmedParticipationRequests = processConfirmedParticipationRequests(participationRequestList, oversizeForRequests);
                rejectedParticipationRequests = processRejectedParticipationRequests(participationRequestList, oversizeForRequests);
            } else {
                for (ParticipationRequest participationRequest : participationRequestList) {
                    participationRequest.setStatus(RequestState.CONFIRMED);
                    confirmedParticipationRequests.add(participationRequest);
                }
            }
            participationRequestRepository.saveAll(participationRequestList);
            updateEventConfirmedRequests(event, confirmedParticipationRequests.size());
        } else if (eventRequestStatusUpdateRequest.getStatus() == RequestState.REJECTED) {
            for (ParticipationRequest participationRequest : participationRequestList) {
                participationRequest.setStatus(RequestState.REJECTED);
                rejectedParticipationRequests.add(participationRequest);
            }
            participationRequestRepository.saveAll(participationRequestList);
        }
        return participationRequestMapper.toEventRequestStatusUpdateResult(confirmedParticipationRequests, rejectedParticipationRequests);
    }

    @Override
    @Transactional
    public void updateEventConfirmedRequests(Event event, long value) {
        event.setConfirmedRequests(event.getConfirmedRequests() + value);
        eventRepository.save(event);
    }

    @Override
    public List<Event> finalAllById(List<Long> eventsIds) {
        return eventRepository.findAllById(eventsIds);
    }

    @Override
    public List<EventShortDto> getEventShortDtoListWithStatistic(List<Event> events) {
        Map<Long, Long> eventIdViewsMap = getEventIdViewsMap(events);
        if (eventIdViewsMap.values().stream().allMatch(value -> value.equals(0L))) {
            return eventDtoMapper.toEventShortDtoList(events, eventIdViewsMap).stream()
                    .sorted(Comparator.comparing(EventShortDto::getEventDate))
                    .collect(Collectors.toList());
        }
        return eventDtoMapper.toEventShortDtoList(events, eventIdViewsMap).stream()
                .sorted(Comparator.comparingLong(EventShortDto::getViews))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getEventFullDtoList(List<Event> events) {
        Map<Long, Long> eventIdViewsMap = getEventIdViewsMap(events);
        if (eventIdViewsMap.values().stream().allMatch(value -> value.equals(0L))) {
            return eventDtoMapper.toEventFullDtoList(events, eventIdViewsMap).stream()
                    .sorted(Comparator.comparing(EventFullDto::getEventDate))
                    .collect(Collectors.toList());
        }
        return eventDtoMapper.toEventFullDtoList(events, eventIdViewsMap).stream()
                .sorted(Comparator.comparingLong(EventFullDto::getViews))
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, Long> getEventIdViewsMap(List<Event> events) {
        List<Event> notPublishedEvents = new ArrayList<>();
        List<Event> publishedEvents = new ArrayList<>();
        LocalDateTime startDateForStats = LocalDateTime.now();

        for (Event event : events) {
            if (event.getState() == EventState.PUBLISHED) {
                publishedEvents.add(event);
                if (event.getPublishedOn() != null && event.getPublishedOn().isBefore(startDateForStats)) {
                    startDateForStats = event.getPublishedOn();
                }
            } else {
                notPublishedEvents.add(event);
            }
        }
        log.debug("notPublishedEvents size = {}", notPublishedEvents.size());
        log.debug("publishedEvents size = {}", publishedEvents.size());

        Map<Long, Long> eventIdViewsMap = new HashMap<>();
        if (!notPublishedEvents.isEmpty()) {
            for (Event notPublishedEvent : notPublishedEvents) {
                eventIdViewsMap.put(notPublishedEvent.getId(), 0L);
            }
        }
        if (publishedEvents.isEmpty()) {
            return eventIdViewsMap;
        }
        return processPublishedEvents(startDateForStats, eventIdViewsMap, publishedEvents);
    }

    @Override
    public List<EventShortDto> getEventsByParameters(SearchParametersPublicRequest searchParametersPublicRequest) {
        List<Event> eventsList = eventRepository.findAll(getPredicate(searchParametersPublicRequest),
                searchParametersPublicRequest.getPageable()).getContent();


        if (searchParametersPublicRequest.getSortOption() == SortOption.EVENT_DATE) {
            return getEventShortDtoListWithStatistic(eventsList).stream()
                    .sorted(Comparator.comparing(EventShortDto::getEventDate)).collect(Collectors.toList());
        }
        return getEventShortDtoListWithStatistic(eventsList).stream()
                .sorted(Comparator.comparingLong(EventShortDto::getViews)).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} could not be found", eventId);
            return new ObjectNotFoundException("The required object was not found.", String.format("Event with id=%d was not found", eventId));
        });
        if (event.getState() != EventState.PUBLISHED) {
            log.error("Only published events can be requested, event state = {}", event.getState().name());
            throw new IncorrectEventStateException("Incorrect event State",
                    String.format("Only published events can be requested, event state = %s", event.getState().name()));
        }
        return getEventFullDtoWithStatistic(event);

    }

    @Transactional
    @Override
    public EventFullDto updateEventByAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} could not be found", eventId);
            return new ObjectNotFoundException("The required object was not found.", String.format("Event with id=%d was not found", eventId));
        });
        checkUpdateRequirements(event, updateEventAdminRequest);
        Event updatedEvent = eventRepository.save(updateEventFields(event, updateEventAdminRequest));
        return getEventFullDtoWithStatistic(updatedEvent);
    }

    @Override
    public List<EventFullDto> getEventsBySearch(SearchParametersAdminRequest searchParametersAdminRequest) {
        List<Event> eventsList = eventRepository.findAll(getPredicate(searchParametersAdminRequest), searchParametersAdminRequest.getPageable()).getContent();
        return getEventFullDtoList(eventsList);
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
            booleanBuilder.and(qEvent.eventDate.goe(searchParametersAdminRequest.getRangeStart()));
        }
        if (searchParametersAdminRequest.getRangeEnd() != null) {
            booleanBuilder.and(qEvent.eventDate.loe(searchParametersAdminRequest.getRangeEnd()));
        }
        return booleanBuilder;
    }

    private void checkUpdateRequirements(Event event, UpdateEventAdminRequest updateEventAdminRequest) {
        if (updateEventAdminRequest.getStateAction() == EventStateAction.PUBLISH_EVENT) {
            if (event.getState() != EventState.PENDING) {
                log.error("Cannot publish the event because it's not in the right state: {}", event.getState().toString());
                throw new EventAdminUpdateException("For the requested operation the conditions are not met.",
                        String.format("Cannot publish the event because it's not in the right state: %s", event.getState().toString()));
            }
            if (LocalDateTime.now().plusHours(1).isAfter(event.getEventDate())) {
                log.error("Event cannot be published due to its start in less than 1 hour, event.getEventDate() = {}," +
                        " LocalDateTime.now().plusHours(1) {}", event.getEventDate(), LocalDateTime.now().plusHours(1));
                throw new EventAdminUpdateException("For the requested operation the conditions are not met.",
                        ("Event cannot be published due to its start in less than 1 hour"));
            }
        } else if (updateEventAdminRequest.getStateAction() == EventStateAction.REJECT_EVENT) {
            if (event.getState() != EventState.PENDING) {
                log.error("Cannot reject the event because it's not in the right state: {}", event.getState().toString());
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
                    log.error("Category with id {} could not be found", newCategoryId);
                    return new ObjectNotFoundException("The required object was not found.", String.format("Category with id=%d was not found", newCategoryId));
                });
                event.setCategory(newCategory);
            }
        }
        return event;
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
            booleanBuilder.and(qEvent.eventDate.goe(searchParametersPublicRequest.getRangeStart()));
        }
        if (searchParametersPublicRequest.getRangeEnd() != null) {
            booleanBuilder.and(qEvent.eventDate.loe(searchParametersPublicRequest.getRangeEnd()));
        }
        if (searchParametersPublicRequest.getRangeStart() == null && searchParametersPublicRequest.getRangeEnd() == null) {
            booleanBuilder.and(qEvent.eventDate.goe(LocalDateTime.now()));
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

    private void checkExceptionalCasesWhenProcessRequests(Event event) {
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            log.error("Event does not require participation requests approval. ParticipantLimit = {}, isRequestModeration = {}",
                    event.getParticipantLimit(), event.isRequestModeration());
            throw new ParticipationRequestProcessingException("Event does not require participation requests approval",
                    String.format("Event does not require participation requests approval. ParticipantLimit = %d, isRequestModeration = %s",
                            event.getParticipantLimit(), event.isRequestModeration()));
        }
        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            log.error("For the requested operation the conditions are not met. ParticipantLimit = {}, ConfirmedRequests = {}",
                    event.getParticipantLimit(), event.getConfirmedRequests());
            throw new ParticipationRequestProcessingException("For the requested operation the conditions are not met.",
                    "The participant limit has been reached");
        }
    }

    private Map<Long, Long> processPublishedEvents(LocalDateTime startDateForStats,
                                                   Map<Long, Long> eventIdViewsMap, List<Event> publishedEvents) {
        Map<String, Long> eventIdUriMap = new HashMap<>();
        for (Event publishedEvent : publishedEvents) {
            eventIdUriMap.put("/events/" + publishedEvent.getId(), publishedEvent.getId());
        }

        StatsParamsDto statsParamsDto = new StatsParamsDto(startDateForStats, LocalDateTime.now(),
                new ArrayList<>(eventIdUriMap.keySet()), false);
        List<StatsToUserDto> statsList = statsRecordingService.getStats(statsParamsDto);
        log.debug("statsList size = {}", statsList.size());

        for (StatsToUserDto statsToUserDto : statsList) {
            String uri = statsToUserDto.getUri();
            Long hits = statsToUserDto.getHits();
            Long eventId = eventIdUriMap.get(uri);
            eventIdViewsMap.put(eventId, hits);
        }
        for (Long eventId : eventIdUriMap.values()) {
            if (!eventIdViewsMap.containsKey(eventId)) {
                eventIdViewsMap.put(eventId, 0L);
            }
        }
        return eventIdViewsMap;
    }


    private List<StatsToUserDto> makeClientRequest(Event event) {
        LocalDateTime start = event.getPublishedOn();
        String uri = "/events/" + event.getId();
        StatsParamsDto statsParamsDto = new StatsParamsDto(start, LocalDateTime.now(), List.of(uri), true);
        return statsRecordingService.getStats(statsParamsDto);
    }

    private Event updateEventFields(Event event, UpdateEventUserRequest updateEventUserRequest) {
        event.setAnnotation(Objects.requireNonNullElse(updateEventUserRequest.getAnnotation(), event.getAnnotation()));
        event.setDescription(Objects.requireNonNullElse(updateEventUserRequest.getDescription(), event.getDescription()));
        event.setEventDate(Objects.requireNonNullElse(updateEventUserRequest.getEventDate(), event.getEventDate()));
        event.setLocation(Objects.requireNonNullElse(updateEventUserRequest.getLocation(), event.getLocation()));
        event.setPaid(Objects.requireNonNullElse(updateEventUserRequest.getPaid(), event.isPaid()));
        event.setParticipantLimit(Objects.requireNonNullElse(updateEventUserRequest.getParticipantLimit(), event.getParticipantLimit()));
        event.setRequestModeration(Objects.requireNonNullElse(updateEventUserRequest.getRequestModeration(), event.isRequestModeration()));
        event.setTitle(Objects.requireNonNullElse(updateEventUserRequest.getTitle(), event.getTitle()));
        if (updateEventUserRequest.getStateAction() == EventStateAction.SEND_TO_REVIEW) {
            event.setState(EventState.PENDING);
        }
        if (updateEventUserRequest.getStateAction() == EventStateAction.CANCEL_REVIEW) {
            event.setState(EventState.CANCELED);
        }
        Long newCategoryId = updateEventUserRequest.getCategory();
        if (newCategoryId != null) {
            if (!event.getCategory().getId().equals(newCategoryId)) {
                Category newCategory = categoryRepository.findById(newCategoryId).orElseThrow(() -> {
                    log.error("Category with id {} could not be found", newCategoryId);
                    return new ObjectNotFoundException("The required object was not found.",
                            String.format("Category with id=%d was not found", newCategoryId));
                });
                event.setCategory(newCategory);
            }
        }
        return event;
    }

    private List<ParticipationRequest> processRejectedParticipationRequests(List<ParticipationRequest> participationRequestList,
                                                                            int oversizeForRequests) {
        List<ParticipationRequest> rejectedParticipationRequests = participationRequestList.subList(oversizeForRequests,
                participationRequestList.size());
        for (ParticipationRequest participationRequest : rejectedParticipationRequests) {
            participationRequest.setStatus(RequestState.REJECTED);
        }
        return rejectedParticipationRequests;
    }

    private List<ParticipationRequest> processConfirmedParticipationRequests(List<ParticipationRequest> participationRequestList,
                                                                             int oversizeForRequests) {
        List<ParticipationRequest> confirmedParticipationRequests = participationRequestList.subList(0, oversizeForRequests);
        for (ParticipationRequest participationRequest : confirmedParticipationRequests) {
            participationRequest.setStatus(RequestState.CONFIRMED);
        }
        return confirmedParticipationRequests;
    }

    private void checkExceptionalCasesWhenUpdateEvent(Event event, UpdateEventUserRequest updateEventUserRequest) {
        if (event.getState() == EventState.PUBLISHED) {
            log.error("Event with EventState = PUBLISHED cannot be updated");
            throw new EventUpdatingException("For the requested operation the conditions are not met.",
                    "Only pending or cancelled events can be changed");
        }
        if (updateEventUserRequest.getStateAction() != EventStateAction.CANCEL_REVIEW
                && updateEventUserRequest.getStateAction() != EventStateAction.SEND_TO_REVIEW) {
            log.error("User cannot change state of event other than cancel it or send for review");
            throw new EventUpdatingException("For the requested operation the conditions are not met.",
                    "User cannot change state of event other than cancel it or send for review");
        }
    }
}
