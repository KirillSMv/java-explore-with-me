package ru.practicum.statsServer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statsDto.GetStatsParamsDto;
import ru.practicum.statsDto.NewStatsDto;
import ru.practicum.statsServer.mapper.StatsDtoMapper;
import ru.practicum.statsServer.storage.StatsDtoToUser;
import ru.practicum.statsServer.storage.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final StatsDtoMapper statsDtoMapper;

    @Override
    @Transactional
    public void saveStats(NewStatsDto newStatsDto) {
        statsRepository.save(statsDtoMapper.toStatistic(newStatsDto));
    }

    @Override
    public List<StatsDtoToUser> getStats(GetStatsParamsDto getStatsParamsDto) {
        LocalDateTime start = getStatsParamsDto.getStart();
        LocalDateTime end = getStatsParamsDto.getEnd();
        List<String> uris = getStatsParamsDto.getUris();
        boolean unique = getStatsParamsDto.isUnique();

        if (uris == null && !unique) {
            return statsRepository.findAllByTimestampBetweenOrderByHitsDesc(start, end);
        } else if (uris != null && unique) {
            return statsRepository.findAllDistinctByIpAndUriInAndTimestampBetween(uris, start, end);
        } else if (uris == null) {
            return statsRepository.findAllDistinctByIpAndTimestampBetween(start, end);
        } else {
            return statsRepository.findAllByUriInAndTimestampBetweenOrderByHitsDesc(uris, start, end);
        }
    }
}
