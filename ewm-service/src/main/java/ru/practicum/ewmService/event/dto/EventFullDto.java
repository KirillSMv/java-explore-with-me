package ru.practicum.ewmService.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmService.category.dto.CategoryDto;
import ru.practicum.ewmService.event.enums.EventState;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private LocationShortDto location;
    private boolean paid;
    private Integer participantLimit;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn; //Дата и время публикации события
    private boolean requestModeration;
    private EventState state;
    private String title;
    private Long views; //количество просмотров события

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserShortDto {
        private Long id;
        private String name;
    }
}
