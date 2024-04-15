package ru.practicum.ewmService.request.dto.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmService.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewmService.request.dto.ParticipationRequestDto;
import ru.practicum.ewmService.request.model.ParticipationRequest;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ParticipationRequestMapper {

/*    public ParticipationRequest toParticipationRequest(ParticipationRequestDto participationRequestDto, User user, Event event) {
        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setRequester(user);
        participationRequest.setEvent(event);
        participationRequest.setStatus(participationRequestDto.getStatus());
        return participationRequest;
    }*/

    public ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return new ParticipationRequestDto(
                participationRequest.getId(),
                participationRequest.getEvent().getId(),
                participationRequest.getCreated(),
                participationRequest.getRequester().getId(),
                participationRequest.getStatus());
    }

    public List<ParticipationRequestDto> toParticipationRequestDtoList(List<ParticipationRequest> requests) {
        return requests.stream().map(this::toParticipationRequestDto).collect(Collectors.toList());
    }

    public EventRequestStatusUpdateResult toEventRequestStatusUpdateResult(List<ParticipationRequest> confirmedParticipationRequests,
                                                                           List<ParticipationRequest> rejectedParticipationRequests) {
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        eventRequestStatusUpdateResult.setConfirmedRequests(confirmedParticipationRequests.stream().map(this::toParticipationRequestDto).collect(Collectors.toList()));
        eventRequestStatusUpdateResult.setRejectedRequests(rejectedParticipationRequests.stream().map(this::toParticipationRequestDto).collect(Collectors.toList()));
        return eventRequestStatusUpdateResult;
    }


}
