package ru.practicum.ewmService.request.service;

import ru.practicum.ewmService.request.dto.ParticipationRequestDto;

public interface PrivateParticipationRequestService {

    ParticipationRequestDto addParticipationRequest(Long userId, Long eventId,
                                                    ParticipationRequestDto participationRequestDto);


}
