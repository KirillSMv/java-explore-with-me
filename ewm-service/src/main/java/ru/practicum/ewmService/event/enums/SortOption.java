package ru.practicum.ewmService.event.enums;

import java.util.Optional;

public enum SortOption {
    EVENT_DATE, VIEWS;

    public static Optional<SortOption> convert(String sort) {
        for (SortOption option : SortOption.values()) {
            if (option.name().equals(sort)) {
                return Optional.of(option);
            }
        }
        return Optional.empty();
    }
    }
