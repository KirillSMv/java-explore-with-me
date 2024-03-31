package ru.practicum.statsServer.storage;

public interface StatsDtoToUser {
    String getApp();

    String getUri();

    Long getHits();
}
