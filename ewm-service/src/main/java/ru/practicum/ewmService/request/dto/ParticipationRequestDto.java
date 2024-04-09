package ru.practicum.ewmService.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmService.request.enums.RequestState;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {
    private Long id;

    private Long event;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime created;

    private Long requester;

    private RequestState status;
}
