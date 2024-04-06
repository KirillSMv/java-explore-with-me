package ru.practicum.ewmService.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestStatusUpdateResult {
    //Результат подтверждения/отклонения заявок на участие в событии
    private ParticipationRequestDto confirmedRequests;
    private ParticipationRequestDto rejectedRequests;
}
