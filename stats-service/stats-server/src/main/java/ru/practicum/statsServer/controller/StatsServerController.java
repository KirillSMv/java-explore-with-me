package ru.practicum.statsServer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsDto.StatsParamsDto;
import ru.practicum.statsDto.NewStatsDto;
import ru.practicum.statsDto.StatsToUserDto;
import ru.practicum.statsServer.service.StatsService;

import javax.validation.Valid;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsServerController {

    private final StatsService service;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/hit")
    public StatsToUserDto postStatistics(@RequestBody @Valid NewStatsDto newStatsDto) {
        log.info("method postStatistics, parameter = {}", newStatsDto);
        return service.saveStats(newStatsDto);
    }

    @GetMapping("/stats")
    public List<StatsToUserDto> getStats(@RequestParam("start") String start,
                                   @RequestParam("end") String end,
                                   @RequestParam(name = "uris", required = false) List<String> uris,
                                   @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        log.info("method getStats, params: start = {}, end = {}, uris = {}, unique = {}", start, end, uris, unique);
        LocalDateTime startTime = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), formatter);
        LocalDateTime endTime = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), formatter);
        return service.getStats(new StatsParamsDto(startTime, endTime, uris, unique));
    }

}
