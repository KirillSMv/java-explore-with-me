package ru.practicum.ewmService.event.enums;

import java.util.ArrayList;
import java.util.List;

public enum EventState {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static List<EventState> convertToEventState(List<String> states) {

        List<EventState> eventStatesList = new ArrayList<>();
        for (String state : states) {
            EventState convertedState = null;
            for (EventState eventState : EventState.values()) {
                if (eventState.name().equals(state.toUpperCase())) {
                    convertedState = eventState;
                    eventStatesList.add(eventState);
                }
            }
            if (convertedState == null) {
                throw new IllegalArgumentException(("Unknown state: " + state));
            }
        }
        return eventStatesList;
    }
}
