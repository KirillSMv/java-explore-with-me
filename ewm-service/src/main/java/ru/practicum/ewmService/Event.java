package ru.practicum.ewmService;

import java.time.LocalDateTime;

public class Event {
    Long id;
    String annotation;
    String title;
    String description;
    Category category; // (@ManyToOne)
    Integer confirmedRequests;
    LocalDateTime eventDate;
    LocalDateTime createdOn;
    LocalDateTime publishedOn;
    User initiator; // (@ManyToOne)
    Boolean paid;
    Boolean requestModeration;
    EventState state = EventState.PENDING;
    Integer participantLimit;
    Location location;


}
