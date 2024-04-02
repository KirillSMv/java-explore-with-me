package ru.practicum.ewmService.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmService.category.dto.CategoryDto;
import ru.practicum.ewmService.user.dto.UserShortDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    Long id;
    String annotation;
    CategoryDto category;
    Long confirmedRequests;
    String eventDate; //"yyyy-MM-dd HH:mm:ss"
    UserShortDto initiator;
    Boolean paid;
    String title;
    Long views;
}
