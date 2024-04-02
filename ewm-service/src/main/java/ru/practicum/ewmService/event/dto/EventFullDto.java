package ru.practicum.ewmService.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmService.category.dto.CategoryDto;
import ru.practicum.ewmService.event.Location;
import ru.practicum.ewmService.event.enums.EventState;
import ru.practicum.ewmService.user.dto.UserShortDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    Long id;
    String annotation;
    CategoryDto category;
    Long confirmedRequests;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    String createdOn;
    String description;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    String eventDate;
    UserShortDto initiator;
    Location location;
    Boolean paid;
    Integer participantLimit;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    String publishedOn; //Дата и время публикации события
    Boolean requestModeration;
    EventState state;
    String title;
    Long views; //количество просмотров события
}
