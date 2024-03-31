package ru.practicum.statsServer.model;

public interface StatsDtoToUser {
    String getApp();

    String getUri();

    Long getHits();
}
