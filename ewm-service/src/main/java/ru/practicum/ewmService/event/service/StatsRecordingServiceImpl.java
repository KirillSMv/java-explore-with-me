package ru.practicum.ewmService.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewmService.event.service.interfaces.StatsRecordingService;
import ru.practicum.ewmService.statClient.StatClient;
import ru.practicum.statsDto.NewStatsDto;
import ru.practicum.statsDto.StatsParamsDto;
import ru.practicum.statsDto.StatsToUserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsRecordingServiceImpl implements StatsRecordingService {

    private final StatClient statClient;

    @Override
    public StatsToUserDto postStats(NewStatsDto newStatsDto) {
        return statClient.postStats(newStatsDto);
    }

    @Override
    public List<StatsToUserDto> getStats(StatsParamsDto statsParamsDto) {
        return statClient.getStats(statsParamsDto);
    }
}
