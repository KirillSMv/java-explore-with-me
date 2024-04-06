package ru.practicum.ewmService.compilation;

import ru.practicum.ewmService.event.Event;

import java.util.Set;

public class Compilation {
    Long id;
    Boolean pinned;
    String title;
    Set<Event> events;
}
