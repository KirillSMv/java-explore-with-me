package ru.practicum.ewmService.event.service.interfaces;

import ru.practicum.statsDto.NewStatsDto;
import ru.practicum.statsDto.StatsParamsDto;
import ru.practicum.statsDto.StatsToUserDto;

import java.util.List;

public interface StatsRecordingService {
    StatsToUserDto postStats(NewStatsDto newStatsDto);

    List<StatsToUserDto> getStats(StatsParamsDto statsParamsDto);

}
