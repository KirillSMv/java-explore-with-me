package ru.practicum.ewmService.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResult {
    //Результат подтверждения/отклонения заявок на участие в событии
    ParticipationRequestDto confirmedRequests;
    ParticipationRequestDto rejectedRequests;


}
