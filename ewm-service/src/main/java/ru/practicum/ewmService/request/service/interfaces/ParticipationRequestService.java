package ru.practicum.ewmService.request.service.interfaces;

import ru.practicum.ewmService.request.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    ParticipationRequestDto addParticipationRequest(Long userId, Long eventId);


    List<ParticipationRequestDto> getOwnParticipationRequests(Long userId);

    ParticipationRequestDto cancelOwnParticipationRequest(Long userId, Long requestId);
}
