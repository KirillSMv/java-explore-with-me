package ru.practicum.ewmService.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmService.request.enums.RequestState;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {
    //заявка на участе в событии
    Long id;
    Long event;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    String created;
    Long requester;
    RequestState status = RequestState.PENDING;
}
