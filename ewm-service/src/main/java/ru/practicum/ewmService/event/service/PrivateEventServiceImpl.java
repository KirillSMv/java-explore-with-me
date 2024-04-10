package ru.practicum.ewmService.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.category.Category;
import ru.practicum.ewmService.category.storage.CategoryRepository;
import ru.practicum.ewmService.event.Event;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.dto.NewEventDto;
import ru.practicum.ewmService.event.dto.UpdateEventUserRequest;
import ru.practicum.ewmService.event.dto.mapper.EventDtoMapper;
import ru.practicum.ewmService.event.enums.EventState;
import ru.practicum.ewmService.event.enums.EventStateAction;
import ru.practicum.ewmService.event.service.interfaces.PrivateEventService;
import ru.practicum.ewmService.event.service.interfaces.StatsRecordingService;
import ru.practicum.ewmService.event.storage.EventRepository;
import ru.practicum.ewmService.exceptions.CustomValidationException;
import ru.practicum.ewmService.exceptions.EventUpdatingException;
import ru.practicum.ewmService.exceptions.ObjectNotFoundException;
import ru.practicum.ewmService.exceptions.ParticipationRequestProcessingException;
import ru.practicum.ewmService.request.ParticipationRequest;
import ru.practicum.ewmService.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewmService.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewmService.request.dto.ParticipationRequestDto;
import ru.practicum.ewmService.request.dto.mapper.ParticipationRequestMapper;
import ru.practicum.ewmService.request.enums.RequestState;
import ru.practicum.ewmService.request.storage.ParticipationRequestRepository;
import ru.practicum.ewmService.user.User;
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
public class PrivateEventServiceImpl implements PrivateEventService {

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
            log.info("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.", String.format("User with id=%d was not found", userId));
        });
        Event event = eventDtoMapper.toEvent(newEventDto);
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> {
            log.info("Category with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.", String.format("Category with id=%d was not found", newEventDto.getCategory()));
        });
        event.setCategory(category);
        event.setInitiator(user);
        log.info("event = {}", event);
        return eventDtoMapper.toEventFullDto(eventRepository.save(event), 0L);
    }

    @Override
    public List<EventShortDto> getEventsAddedByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.", String.format("User with id=%d was not found", userId));
        });
        List<Event> events = eventRepository.findAllByInitiator(user, pageable);

        if (events.isEmpty()) {
            return new ArrayList<>();
        }
        return getEventShortDtoListWithStatistic(events);

        /*List<Event> notPublishedEvents = new ArrayList<>();
        List<Event> publishedEvents = new ArrayList<>();
        LocalDateTime earliestPublicationDate = LocalDateTime.MIN;
        for (Event event : events) {
            if (event.getState() == EventState.PUBLISHED) {
                publishedEvents.add(event);
                if (event.getPublishedOn() != null && event.getPublishedOn().isAfter(earliestPublicationDate)) {
                    earliestPublicationDate = event.getPublishedOn();
                }
            } else {
                notPublishedEvents.add(event);
            }
        }
        log.info("notPublishedEvents = {}", notPublishedEvents);
        log.info("publishedEvents = {}", publishedEvents);

        Map<Long, Long> eventIdViewsMap = new HashMap<>();
        for (Event notPublishedEvent : notPublishedEvents) {
            eventIdViewsMap.put(notPublishedEvent.getId(), 0L);
        }

        if (publishedEvents.isEmpty()) {
            return eventDtoMapper.toEventShortDtoList(events, eventIdViewsMap).stream().sorted(Comparator.comparing(EventShortDto::getEventDate)).collect(Collectors.toList());
        }


        Map<String, Long> eventIdUriMap = new HashMap<>();
        for (Event publishedEvent : publishedEvents) {
            eventIdUriMap.put("/events/" + publishedEvent.getId(), publishedEvent.getId());
        }

        StatsParamsDto statsParamsDto = new StatsParamsDto(earliestPublicationDate, LocalDateTime.now(), new ArrayList<>(eventIdUriMap.keySet()), false);
        List<StatsToUserDto> statsList = statsRecordingService.getStats(statsParamsDto);
        log.info("statsList = {}", statsList);

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
        return eventDtoMapper.toEventShortDtoList(events, eventIdViewsMap).stream().sorted(Comparator.comparingLong(EventShortDto::getViews)).collect(Collectors.toList());
    */
    }

    @Override
    public EventFullDto getDetailedEventAddedByUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.info("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.", String.format("User with id=%d was not found", userId));
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.info("Event with id {} could not be found", eventId);
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
        log.info("statsList = {}", statsList);
        if (statsList.isEmpty()) {
            return eventDtoMapper.toEventFullDto(event, 0L);
        }
        return eventDtoMapper.toEventFullDto(event, statsList.get(0).getHits());
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.info("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.", String.format("User with id=%d was not found", userId));
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.info("Event with id {} could not be found", eventId);
            return new ObjectNotFoundException("The required object was not found.", String.format("Event with id=%d was not found", eventId));
        });
        if (event.getState() == EventState.PUBLISHED) {
            log.info("Event with EventState = PUBLISHED cannot be updated");
            throw new EventUpdatingException("For the requested operation the conditions are not met.", "Only pending or cancelled events can be changed");
        }
        if (updateEventUserRequest.getStateAction() != EventStateAction.CANCEL_REVIEW && updateEventUserRequest.getStateAction() != EventStateAction.SEND_TO_REVIEW) {
            log.info("User cannot change state of event other than cancel it or send for review");
            throw new EventUpdatingException("For the requested operation the conditions are not met.",
                    "User cannot change state of event other than cancel it or send for review");
        }
        Event updatedEvent = updateEventFields(event, updateEventUserRequest);
        return eventDtoMapper.toEventFullDto(updatedEvent, 0L);
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestsFromUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.info("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.", String.format("User with id=%d was not found", userId));
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.info("Event with id {} could not be found", eventId);
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
            log.info("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.", String.format("User with id=%d was not found", userId));
        });

        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.info("Event with id {} could not be found", eventId);
            return new ObjectNotFoundException("The required object was not found.", String.format("Event with id=%d was not found", eventId));
        });
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            log.info("Event does not require participation requests approval. ParticipantLimit = {}, isRequestModeration = {}",
                    event.getParticipantLimit(), event.isRequestModeration());
            throw new ParticipationRequestProcessingException("Event does not require participation requests approval",
                    String.format("Event does not require participation requests approval. ParticipantLimit = %d, isRequestModeration = %s",
                            event.getParticipantLimit(), event.isRequestModeration()));
        }
        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            log.info("For the requested operation the conditions are not met. ParticipantLimit = {}, ConfirmedRequests = {}",
                    event.getParticipantLimit(), event.getConfirmedRequests());
            throw new ParticipationRequestProcessingException("For the requested operation the conditions are not met.",
                    "The participant limit has been reached");
        }
        List<ParticipationRequest> participationRequestList = participationRequestRepository.findAllById(eventRequestStatusUpdateRequest.getRequestIds());
        log.info("eventRequestStatusUpdateRequest.getRequestIds() = {}", eventRequestStatusUpdateRequest.getRequestIds());
        log.info("participationRequestList = {}", participationRequestList);
        boolean isStatusPending = participationRequestList.stream().allMatch(participationRequest -> participationRequest.getStatus() == RequestState.PENDING);
        if (!isStatusPending) {
            log.info("Not all participation requests have status Pending");
            throw new CustomValidationException("Incorrectly made request.", "Request must have status PENDING");
        }

        long confirmedRequests = event.getConfirmedRequests();
        long participantLimit = event.getParticipantLimit();
        List<ParticipationRequest> confirmedParticipationRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedParticipationRequests = new ArrayList<>();

        if (confirmedRequests + participationRequestList.size() > participantLimit) {
            int oversizeForRequests = (int) (participantLimit - confirmedRequests + participationRequestList.size());
            confirmedParticipationRequests = participationRequestList.subList(0, oversizeForRequests);
            rejectedParticipationRequests = participationRequestList.subList(oversizeForRequests, participationRequestList.size());
            for (ParticipationRequest participationRequest : confirmedParticipationRequests) {
                participationRequest.setStatus(RequestState.CONFIRMED);
            }
            for (ParticipationRequest participationRequest : rejectedParticipationRequests) {
                participationRequest.setStatus(RequestState.REJECTED);
            }
        } else {
            for (ParticipationRequest participationRequest : participationRequestList) {
                participationRequest.setStatus(RequestState.CONFIRMED);
                confirmedParticipationRequests.add(participationRequest);
            }
        }
        log.info("participationRequestList = {}", participationRequestList);
        log.info("confirmedParticipationRequests = {}", confirmedParticipationRequests);
        log.info("rejectedParticipationRequests = {}", rejectedParticipationRequests);

        participationRequestRepository.saveAll(participationRequestList);
        updateEventConfirmedRequests(event, confirmedParticipationRequests.size());
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
            return eventDtoMapper.toEventShortDtoList(events, eventIdViewsMap).stream().sorted(Comparator.comparing(EventShortDto::getEventDate)).collect(Collectors.toList());
        }
        return eventDtoMapper.toEventShortDtoList(events, eventIdViewsMap).stream().sorted(Comparator.comparingLong(EventShortDto::getViews)).collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getEventFullDtoList(List<Event> events) {
        Map<Long, Long> eventIdViewsMap = getEventIdViewsMap(events);
        if (eventIdViewsMap.values().stream().allMatch(value -> value.equals(0L))) {
            return eventDtoMapper.toEventFullDtoList(events, eventIdViewsMap).stream().sorted(Comparator.comparing(EventFullDto::getEventDate)).collect(Collectors.toList());
        }
        return eventDtoMapper.toEventFullDtoList(events, eventIdViewsMap).stream().sorted(Comparator.comparingLong(EventFullDto::getViews)).collect(Collectors.toList());
    }

    @Override
    public Map<Long, Long> getEventIdViewsMap(List<Event> events) {
        List<Event> notPublishedEvents = new ArrayList<>();
        List<Event> publishedEvents = new ArrayList<>();
        LocalDateTime earliestPublicationDate = LocalDateTime.MIN;

        for (Event event : events) {
            if (event.getState() == EventState.PUBLISHED) {
                publishedEvents.add(event);
                if (event.getPublishedOn() != null && event.getPublishedOn().isAfter(earliestPublicationDate)) {
                    earliestPublicationDate = event.getPublishedOn();
                }
            } else {
                notPublishedEvents.add(event);
            }
        }
        log.info("notPublishedEvents = {}", notPublishedEvents);
        log.info("publishedEvents = {}", publishedEvents);

        Map<Long, Long> eventIdViewsMap = new HashMap<>();
        if (!notPublishedEvents.isEmpty()) {
            for (Event notPublishedEvent : notPublishedEvents) {
                eventIdViewsMap.put(notPublishedEvent.getId(), 0L);
            }
        }
        if (publishedEvents.isEmpty()) {
            return eventIdViewsMap;
        }
        return processPublishedEvents(earliestPublicationDate, eventIdViewsMap, publishedEvents);
    }


    private Map<Long, Long> processPublishedEvents(LocalDateTime earliestPublicationDate,
                                                   Map<Long, Long> eventIdViewsMap, List<Event> publishedEvents) {
        Map<String, Long> eventIdUriMap = new HashMap<>();
        for (Event publishedEvent : publishedEvents) {
            eventIdUriMap.put("/events/" + publishedEvent.getId(), publishedEvent.getId());
        }

        StatsParamsDto statsParamsDto = new StatsParamsDto(earliestPublicationDate, LocalDateTime.now(),
                new ArrayList<>(eventIdUriMap.keySet()), false);
        List<StatsToUserDto> statsList = statsRecordingService.getStats(statsParamsDto);
        log.info("statsList = {}", statsList);

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


/*    private List<EventShortDto> processPublishedEvents(List<Event> events, LocalDateTime earliestPublicationDate,
                                                       Map<Long, Long> eventIdViewsMap, List<Event> publishedEvents) {
        Map<String, Long> eventIdUriMap = new HashMap<>();
        for (Event publishedEvent : publishedEvents) {
            eventIdUriMap.put("/events/" + publishedEvent.getId(), publishedEvent.getId());
        }

        StatsParamsDto statsParamsDto = new StatsParamsDto(earliestPublicationDate, LocalDateTime.now(), new ArrayList<>(eventIdUriMap.keySet()), false);
        List<StatsToUserDto> statsList = statsRecordingService.getStats(statsParamsDto);
        log.info("statsList = {}", statsList);

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
        return eventDtoMapper.toEventShortDtoList(events, eventIdViewsMap).stream().sorted(Comparator.comparingLong(EventShortDto::getViews)).collect(Collectors.toList());
    }*/

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
                    log.info("Category with id {} could not be found", newCategoryId);
                    return new ObjectNotFoundException("The required object was not found.", String.format("Category with id=%d was not found", newCategoryId));
                });
                event.setCategory(newCategory);
            }
        }
        return event;
    }
}
