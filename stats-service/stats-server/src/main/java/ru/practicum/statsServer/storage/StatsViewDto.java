package ru.practicum.statsServer.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatsViewDto {

    private String app;

    private String uri;

    private Long hits;
}
