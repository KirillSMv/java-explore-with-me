package ru.practicum.statsServer.service;

import ru.practicum.statsDto.NewStatsDto;
import ru.practicum.statsDto.StatsParamsDto;
import ru.practicum.statsDto.StatsToUserDto;

import java.util.List;

public interface StatsService {

    StatsToUserDto saveStats(NewStatsDto newStatsDto);

    List<StatsToUserDto> getStats(StatsParamsDto statsParamsDto);
}
