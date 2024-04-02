package ru.practicum.statsServer.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.statsDto.NewStatsDto;
import ru.practicum.statsServer.model.Statistic;

@Component
public class StatsDtoMapper {

    public Statistic toStatistic(NewStatsDto newStatsDto) {
        Statistic statistic = new Statistic();
        statistic.setApp(newStatsDto.getApp());
        statistic.setIp(newStatsDto.getIp());
        statistic.setUri(newStatsDto.getUri());
        statistic.setTimestamp(newStatsDto.getTimestamp());
        return statistic;
    }
}
