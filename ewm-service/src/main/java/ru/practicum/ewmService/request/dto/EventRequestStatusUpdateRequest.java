package ru.practicum.ewmService.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmService.request.enums.RequestState;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    @NotNull
    private List<Long> requestIds;

    @NotNull
    private RequestState status;
}
