package ru.practicum.statsServer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statsDto.StatsParamsDto;
import ru.practicum.statsDto.NewStatsDto;
import ru.practicum.statsDto.StatsToUserDto;
import ru.practicum.statsServer.mapper.StatsDtoMapper;
import ru.practicum.statsServer.model.Statistic;
import ru.practicum.statsServer.storage.StatsDtoToUser;
import ru.practicum.statsServer.storage.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final StatsDtoMapper statsDtoMapper;

    @Override
    @Transactional
    public StatsToUserDto saveStats(NewStatsDto newStatsDto) {
        Statistic savedStatistic = statsRepository.save(statsDtoMapper.toStatistic(newStatsDto));
        log.info("savedStatistic = {}", savedStatistic);
        return statsDtoMapper.toStatsToUserDto(savedStatistic);
    }

    @Override
    public List<StatsToUserDto> getStats(StatsParamsDto statsParamsDto) {
        LocalDateTime start = statsParamsDto.getStart();
        LocalDateTime end = statsParamsDto.getEnd();
        List<String> uris = statsParamsDto.getUris();
        boolean unique = statsParamsDto.isUnique();
        List<StatsDtoToUser> stats;

        if (uris == null && !unique) {
            stats = statsRepository.findAllByTimestampBetweenOrderByHitsDesc(start, end);
        } else if (uris != null && unique) {
            stats = statsRepository.findAllDistinctByIpAndUriInAndTimestampBetween(uris, start, end);
        } else if (uris == null) {
            stats = statsRepository.findAllDistinctByIpAndTimestampBetween(start, end);
        } else {
            stats = statsRepository.findAllByUriInAndTimestampBetweenOrderByHitsDesc(uris, start, end);
        }
        log.info("List<StatsDtoToUser> stats = {}", stats);
        return statsDtoMapper.toStatsToUserDtoList(stats);
    }
}
