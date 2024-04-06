package ru.practicum.ewmService.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmService.category.Category;
import ru.practicum.ewmService.event.enums.EventState;
import ru.practicum.ewmService.user.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    Long id;
    String annotation;
    String title;
    String description;
    Category category; // (@ManyToOne)
    Long confirmedRequests;
    LocalDateTime eventDate; //String
    LocalDateTime createdOn; //String
    LocalDateTime publishedOn; //String
    User initiator; // (@ManyToOne)
    Boolean paid;
    Boolean requestModeration;
    EventState state = EventState.PENDING;
    Integer participantLimit;
    Location location;
}
