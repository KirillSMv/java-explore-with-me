package ru.practicum.statsServer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statsDto.GetStatsParamsDto;
import ru.practicum.statsDto.NewStatsDto;
import ru.practicum.statsServer.mapper.StatsDtoMapper;
import ru.practicum.statsServer.model.StatsDtoToUser;
import ru.practicum.statsServer.storage.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public void saveStats(NewStatsDto newStatsDto) {
        statsRepository.save(StatsDtoMapper.toStatistic(newStatsDto));
    }

    @Override
    public List<StatsDtoToUser> getStats(GetStatsParamsDto getStatsParamsDto) {
        LocalDateTime start = getStatsParamsDto.getStart();
        LocalDateTime end = getStatsParamsDto.getEnd();
        List<String> uris = getStatsParamsDto.getUris();
        Boolean unique = getStatsParamsDto.getUnique();

        if (uris == null && unique.equals(false)) {
            return statsRepository.findAllByTimestampBetweenOrderByHitsDesc(start, end);
        } else if (uris != null && unique.equals(true)) {
            return statsRepository.findAllDistinctByIpAndUriInAndTimestampBetween(uris, start, end);

        } else if (uris == null && unique.equals(true)) {
            return statsRepository.findAllDistinctByIpAndTimestampBetween(start, end);
        } else {
            return statsRepository.findAllByUriInAndTimestampBetweenOrderByHitsDesc(uris, start, end);
        }
    }
}
