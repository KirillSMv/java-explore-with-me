package ru.practicum.ewmService.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.event.Event;
import ru.practicum.ewmService.event.enums.EventState;
import ru.practicum.ewmService.event.service.interfaces.PrivateEventService;
import ru.practicum.ewmService.event.storage.EventRepository;
import ru.practicum.ewmService.exceptions.ObjectNotFoundException;
import ru.practicum.ewmService.exceptions.ParticipationRequestProcessingException;
import ru.practicum.ewmService.request.ParticipationRequest;
import ru.practicum.ewmService.request.dto.ParticipationRequestDto;
import ru.practicum.ewmService.request.dto.mapper.ParticipationRequestMapper;
import ru.practicum.ewmService.request.enums.RequestState;
import ru.practicum.ewmService.request.service.interfaces.PrivateParticipationRequestService;
import ru.practicum.ewmService.request.storage.ParticipationRequestRepository;
import ru.practicum.ewmService.user.User;
import ru.practicum.ewmService.user.storage.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PrivateParticipationRequestServiceImpl implements PrivateParticipationRequestService {

    private final ParticipationRequestRepository participationRequestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final PrivateEventService privateEventService;
    private final ParticipationRequestMapper participationRequestMapper;

    @Override
    @Transactional
    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.",
                    String.format("User with id=%d was not found", userId));
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.info("Event with id {} could not be found", eventId);
            return new ObjectNotFoundException("The required object was not found.",
                    String.format("Event with id=%d was not found", eventId));
        });
        checkParticipationRequirements(event, userId);

        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setEvent(event);
        participationRequest.setRequester(user);

        if (!event.isRequestModeration()) {
            participationRequest.setStatus(RequestState.CONFIRMED);
        }

        ParticipationRequest savedParticipationRequest = participationRequestRepository.save(participationRequest);
        log.info("privateParticipationRequestRepository = {}", participationRequestRepository);

        if (savedParticipationRequest.getStatus() == RequestState.CONFIRMED) {
            privateEventService.updateEventConfirmedRequests(event, 1L);
        }
        return participationRequestMapper.toParticipationRequestDto(savedParticipationRequest);
    }

    @Override
    public List<ParticipationRequestDto> getOwnParticipationRequests(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.",
                    String.format("User with id=%d was not found", userId));
        });
        List<ParticipationRequest> requestsList = participationRequestRepository.findAllByRequester(user);
        log.info("requestsList = {}", requestsList);
        return participationRequestMapper.toParticipationRequestDtoList(requestsList);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelOwnParticipationRequest(Long userId, Long requestId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.",
                    String.format("User with id=%d was not found", userId));
        });
        ParticipationRequest participationRequest = participationRequestRepository
                .findById(requestId).orElseThrow(() -> {
                    log.info("Participation request with id {} could not be found", userId);
                    return new ObjectNotFoundException("The required object was not found.",
                            String.format("Participation request with id=%d was not found", requestId));
                });
        if (!participationRequest.getRequester().equals(user)) {
            throw new ParticipationRequestProcessingException("Participation requirements were not met",
                    "Participation request cannot be cancelled by another user");
        }
        if (participationRequest.getStatus() == RequestState.CONFIRMED) {
            privateEventService.updateEventConfirmedRequests(participationRequest.getEvent(), -1L);
        }
        participationRequest.setStatus(RequestState.CANCELED);
        return participationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
    }


    private void checkParticipationRequirements(Event event, Long userId) {
        if (event.getInitiator().getId().equals(userId)) {
            throw new ParticipationRequestProcessingException("Participation requirements were not met",
                    "Event initiator cannot send request to own Event");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new ParticipationRequestProcessingException("Participation requirements were not met",
                    "Participation request cannot be sent for not published event");
        }
        if (Objects.equals(event.getConfirmedRequests(), Long.valueOf(event.getParticipantLimit()))) {
            throw new ParticipationRequestProcessingException("Participation requirements were not met",
                    "Participants limit has been met");
        }
    }
}
