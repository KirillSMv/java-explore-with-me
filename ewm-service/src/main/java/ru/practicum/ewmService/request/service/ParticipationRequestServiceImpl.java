package ru.practicum.ewmService.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmService.event.enums.EventState;
import ru.practicum.ewmService.event.model.Event;
import ru.practicum.ewmService.event.service.interfaces.EventService;
import ru.practicum.ewmService.event.storage.EventRepository;
import ru.practicum.ewmService.exceptions.ObjectNotFoundException;
import ru.practicum.ewmService.exceptions.ParticipationRequestProcessingException;
import ru.practicum.ewmService.request.dto.ParticipationRequestDto;
import ru.practicum.ewmService.request.dto.mapper.ParticipationRequestMapper;
import ru.practicum.ewmService.request.enums.RequestState;
import ru.practicum.ewmService.request.model.ParticipationRequest;
import ru.practicum.ewmService.request.service.interfaces.ParticipationRequestService;
import ru.practicum.ewmService.request.storage.ParticipationRequestRepository;
import ru.practicum.ewmService.user.model.User;
import ru.practicum.ewmService.user.storage.UserRepository;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository participationRequestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final ParticipationRequestMapper participationRequestMapper;

    @Override
    @Transactional
    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.",
                    String.format("User with id=%d was not found", userId));
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id {} could not be found", eventId);
            return new ObjectNotFoundException("The required object was not found.",
                    String.format("Event with id=%d was not found", eventId));
        });
        checkParticipationRequirements(event, userId);

        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setEvent(event);
        participationRequest.setRequester(user);

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            participationRequest.setStatus(RequestState.CONFIRMED);
        }

        ParticipationRequest savedParticipationRequest = participationRequestRepository.save(participationRequest);
        log.debug("privateParticipationRequestRepository = {}", participationRequestRepository);

        if (savedParticipationRequest.getStatus() == RequestState.CONFIRMED) {
            eventService.updateEventConfirmedRequests(event, 1L);
        }
        return participationRequestMapper.toParticipationRequestDto(savedParticipationRequest);
    }

    @Override
    public List<ParticipationRequestDto> getOwnParticipationRequests(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.",
                    String.format("User with id=%d was not found", userId));
        });
        List<ParticipationRequest> requestsList = participationRequestRepository.findAllByRequester(user);
        log.debug("requestsList size = {}", requestsList.size());
        return participationRequestMapper.toParticipationRequestDtoList(requestsList);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelOwnParticipationRequest(Long userId, Long requestId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} could not be found", userId);
            return new ObjectNotFoundException("The required object was not found.",
                    String.format("User with id=%d was not found", userId));
        });
        ParticipationRequest participationRequest = participationRequestRepository
                .findById(requestId).orElseThrow(() -> {
                    log.error("Participation request with id {} could not be found", userId);
                    return new ObjectNotFoundException("The required object was not found.",
                            String.format("Participation request with id=%d was not found", requestId));
                });
        if (!participationRequest.getRequester().equals(user)) {
            log.error("Participation request cannot be cancelled by another user, requester = {}, user = {}",
                    participationRequest.getRequester(), user);
            throw new ParticipationRequestProcessingException("Participation requirements were not met",
                    "Participation request cannot be cancelled by another user");
        }
        if (participationRequest.getStatus() == RequestState.CONFIRMED) {
            eventService.updateEventConfirmedRequests(participationRequest.getEvent(), -1L);
        }
        participationRequest.setStatus(RequestState.CANCELED);
        return participationRequestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
    }


    private void checkParticipationRequirements(Event event, Long userId) {
        if (event.getInitiator().getId().equals(userId)) {
            log.error("Event initiator cannot send request to own Event");
            throw new ParticipationRequestProcessingException("Participation requirements were not met",
                    "Event initiator cannot send request to own Event");
        }
        if (event.getState() != EventState.PUBLISHED) {
            log.error("Participation request cannot be sent for not published event");
            throw new ParticipationRequestProcessingException("Participation requirements were not met",
                    "Participation request cannot be sent for not published event");
        }
        if (event.getParticipantLimit() != 0) {
            if (event.getConfirmedRequests() == event.getParticipantLimit()) {
                log.error("Participants limit has been met, limit = {}", event.getParticipantLimit());
                throw new ParticipationRequestProcessingException("Participation requirements were not met",
                        "Participants limit has been met");
            }
        }
    }
}
