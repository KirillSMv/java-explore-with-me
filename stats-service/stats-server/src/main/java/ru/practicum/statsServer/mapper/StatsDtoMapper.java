package ru.practicum.statsServer.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.statsDto.NewStatsDto;
import ru.practicum.statsDto.StatsToUserDto;
import ru.practicum.statsServer.model.Statistic;
import ru.practicum.statsServer.storage.StatsViewDto;

import java.util.List;
import java.util.stream.Collectors;

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

    public StatsToUserDto toStatsToUserDto(Statistic statistic) {
        StatsToUserDto statsToUserDto = new StatsToUserDto();
        statsToUserDto.setApp(statistic.getApp());
        statsToUserDto.setUri(statistic.getUri());
        statsToUserDto.setHits(statistic.getHits());
        return statsToUserDto;
    }


    public List<StatsToUserDto> toStatsToUserDtoList(List<StatsViewDto> stats) {
        return stats.stream().map(stat -> new StatsToUserDto(stat.getApp(), stat.getUri(), stat.getHits())).collect(Collectors.toList());
    }
}
