package ru.practicum.ewmService.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmService.event.enums.EventState;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchParametersAdminRequest {
    private List<Long> users;
    private List<EventState> states;
    private List<Long> categories;
    private String rangeStart;
    private String rangeEnd;
    private Pageable pageable;
}
