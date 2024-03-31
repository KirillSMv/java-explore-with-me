package ru.practicum.statsServer.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.statsDto.NewStatsDto;
import ru.practicum.statsServer.model.Statistic;

@Component
public class StatsDtoMapper {

    public static Statistic toStatistic(NewStatsDto newStatsDto) {
        Statistic statistic = new Statistic();
        statistic.setApp(newStatsDto.getApp());
        statistic.setIp(newStatsDto.getIp());
        statistic.setUri(newStatsDto.getUri());
        statistic.setTimestamp(newStatsDto.getTimestamp());
        return statistic;
    }

/*    public static GetStatsDto toGetStatsDto(Statistic statistic) {
        GetStatsDto getStatsDto = new GetStatsDto(
                statistic.getApp(),
                statistic.getUri();
        );
        return getStatsDto;
    }*/
/*
    public static List<GetStatsDto> toGetStatsDtoList(List<Statistic> statistics) {
        List<GetStatsDto> resultList = new ArrayList<>();
        for (Statistic statistic : statistics) {
            resultList.add(toGetStatsDto(statistic));
        }
        return resultList;
    }*/
}
