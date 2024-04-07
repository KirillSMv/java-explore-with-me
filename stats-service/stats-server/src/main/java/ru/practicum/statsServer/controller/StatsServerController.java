package ru.practicum.statsServer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsDto.GetStatsParamsDto;
import ru.practicum.statsDto.NewStatsDto;
import ru.practicum.statsDto.StatsToUserDto;
import ru.practicum.statsServer.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsServerController {

    private final StatsService service;

    @PostMapping("/hit")
    public StatsToUserDto postStatistics(@RequestBody @Valid NewStatsDto newStatsDto) {
        log.info("method postStatistics, parameter = {}", newStatsDto);
        return service.saveStats(newStatsDto);
    }

    @GetMapping("/stats")
    public List<StatsToUserDto> getStats(@RequestParam("start") LocalDateTime start,
                                         @RequestParam("end") LocalDateTime end,
                                         @RequestParam(name = "uris", required = false) List<String> uris,
                                         @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        log.info("method getStats, params: start = {}, end = {}, uris = {}, unique = {}", start, end, uris, unique);
        return service.getStats(new GetStatsParamsDto(start, end, uris, unique));
    }
}
