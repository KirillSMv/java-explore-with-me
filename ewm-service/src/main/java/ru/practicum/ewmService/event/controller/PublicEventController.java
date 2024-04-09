package ru.practicum.ewmService.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.dto.SearchParametersPublicRequest;
import ru.practicum.ewmService.event.enums.SortOption;
import ru.practicum.ewmService.event.service.interfaces.PublicEventService;
import ru.practicum.ewmService.event.service.interfaces.StatsRecordingService;
import ru.practicum.statsDto.NewStatsDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewmService.Constants.APP;
import static ru.practicum.ewmService.Constants.TIME_PATTERN;

@RestController
@Slf4j
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final PublicEventService publicEventService;
    private final StatsRecordingService statsRecordingService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsByParameters(@RequestParam(name = "text", required = false) String text,
                                                                     @RequestParam(name = "categories", required = false) List<Long> categories,
                                                                     @RequestParam(name = "paid", required = false) Boolean paid,
                                                                     @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                                                     @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                                                     @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
                                                                     @RequestParam(name = "sort", required = false) String sort,
                                                                     @RequestParam(value = "from", defaultValue = "0") @Min(value = 0) Integer from,
                                                                     @RequestParam(value = "size", defaultValue = "10") @Min(value = 1) Integer size,
                                                                     HttpServletRequest request) {
        log.info("getEventsByParameters, parameters: text = {}, categories = {}, paid = {}, rangeStart = {}, " +
                        "rangeEnd = {}, onlyAvailable = {}, sort = {}, from = {}, size = {} ", text, categories,
                paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        SortOption sortOption = SortOption.convert(sort.toUpperCase()).orElseThrow(() -> new IllegalArgumentException("Unknown state: " + text));
        List<EventShortDto> eventShortDtoList = publicEventService.getEventsByParameters(new SearchParametersPublicRequest(text, categories, paid, LocalDateTime.parse(rangeStart, TIME_PATTERN),
                LocalDateTime.parse(rangeEnd, TIME_PATTERN), onlyAvailable, sortOption, PageRequest.of(from / size, size)));

        statsRecordingService.postStats(new NewStatsDto(APP, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now()));
        return new ResponseEntity<>(eventShortDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEventById(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("getEventById method, id = {}", id);
        EventFullDto eventFullDto = publicEventService.getEventById(id);
        statsRecordingService.postStats(new NewStatsDto(APP, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now()));
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }


}
