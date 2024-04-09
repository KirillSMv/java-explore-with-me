package ru.practicum.ewmService.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResult {
    //Результат подтверждения/отклонения заявок на участие в событии
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
