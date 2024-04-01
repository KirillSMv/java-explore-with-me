package ru.practicum.ewmService.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmService.event.Event;
import ru.practicum.ewmService.request.enums.RequestState;
import ru.practicum.ewmService.user.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    Long id;
    Event event;  //(@ManyToOne)
    LocalDateTime created;
    User requester; //(@ManyToOne)
    RequestState status;
}
