package ru.practicum.statsServer.service;

import ru.practicum.statsDto.GetStatsParamsDto;
import ru.practicum.statsDto.NewStatsDto;
import ru.practicum.statsServer.storage.StatsDtoToUser;

import java.util.List;

public interface StatsService {

    void saveStats(NewStatsDto newStatsDto);

    List<StatsDtoToUser> getStats(GetStatsParamsDto getStatsParamsDto);
}
