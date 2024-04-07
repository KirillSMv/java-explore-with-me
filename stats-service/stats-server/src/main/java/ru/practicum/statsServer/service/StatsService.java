package ru.practicum.statsServer.service;

import ru.practicum.statsDto.GetStatsParamsDto;
import ru.practicum.statsDto.NewStatsDto;
import ru.practicum.statsDto.StatsToUserDto;
import ru.practicum.statsServer.storage.StatsDtoToUser;

import java.util.List;

public interface StatsService {

    StatsToUserDto saveStats(NewStatsDto newStatsDto);

    List<StatsToUserDto> getStats(GetStatsParamsDto getStatsParamsDto);
}
