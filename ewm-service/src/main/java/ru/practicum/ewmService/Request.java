package ru.practicum.ewmService;

import java.time.LocalDateTime;

public class Request {
    Long id;
    Event event;  //(@ManyToOne)
    LocalDateTime created;
    User requester; //(@ManyToOne)
    RequestState status;
}
