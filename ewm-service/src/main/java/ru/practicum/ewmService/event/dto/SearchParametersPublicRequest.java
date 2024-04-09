package ru.practicum.ewmService.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmService.event.enums.SortOption;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchParametersPublicRequest {
    private  String text;
    private List<Long> categories;
    private boolean isPaid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private boolean onlyAvailable;
    private SortOption sortOption;
    private Pageable pageable;

}
