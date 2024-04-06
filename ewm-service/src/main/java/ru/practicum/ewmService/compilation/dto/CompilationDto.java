package ru.practicum.ewmService.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmService.event.dto.EventShortDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private Long id;
    private EventShortDto events;
    private Boolean pinned;
    private String title;
}
