package ru.practicum.ewmService.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmService.event.dto.EventFullDto;
import ru.practicum.ewmService.event.dto.EventShortDto;
import ru.practicum.ewmService.event.dto.SearchParametersPublicRequest;
import ru.practicum.ewmService.event.enums.SortOption;
import ru.practicum.ewmService.event.service.interfaces.EventService;
import ru.practicum.ewmService.event.service.interfaces.StatsRecordingService;
import ru.practicum.ewmService.exceptions.CustomValidationException;
import ru.practicum.statsDto.NewStatsDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewmService.constans.Constants.APP;
import static ru.practicum.ewmService.constans.Constants.TIME_PATTERN;

@RestController
@Slf4j
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class PublicEventController {

    private final EventService eventService;
    private final StatsRecordingService statsRecordingService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsByParameters(@RequestParam(name = "text", required = false) String text,
                                                                     @RequestParam(name = "categories", required = false) List<Long> categories,
                                                                     @RequestParam(name = "paid", required = false) boolean paid,
                                                                     @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                                                     @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                                                     @RequestParam(name = "onlyAvailable", required = false) boolean onlyAvailable,
                                                                     @RequestParam(name = "sort", required = false) String sort,
                                                                     @RequestParam(value = "from", defaultValue = "0") @Min(value = 0) Integer from,
                                                                     @RequestParam(value = "size", defaultValue = "10") @Min(value = 1) Integer size,
                                                                     HttpServletRequest request) {
        log.info("getEventsByParameters, parameters: text = {}, categories = {}, paid = {}, rangeStart = {}, " +
                        "rangeEnd = {}, onlyAvailable = {}, sort = {}, from = {}, size = {} ", text, categories,
                paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        SearchParametersPublicRequest searchParametersPublicRequest = checkParameters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        List<EventShortDto> eventShortDtoList = eventService.getEventsByParameters(searchParametersPublicRequest);
        statsRecordingService.postStats(new NewStatsDto(APP, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now()));
        return new ResponseEntity<>(eventShortDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEventById(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("getEventById method, id = {}", id);
        EventFullDto eventFullDto = eventService.getEventById(id);
        statsRecordingService.postStats(new NewStatsDto(APP, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now()));
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }


    @GetMapping("/location/{locId}")
    public ResponseEntity<List<EventShortDto>> getEventsByLocation(@PathVariable("locId") Long locId) {
        log.info("searchByLocation method, locId = {}", locId);
        List<EventShortDto> eventShortDtoList = eventService.getEventsByLocation(locId);
        return new ResponseEntity<>(eventShortDtoList, HttpStatus.OK);
    }

    @GetMapping("/location")
    public ResponseEntity<List<EventShortDto>> getEventsByLocationName(@RequestParam("text") String text) {
        log.info("getEventsByLocationName method, text = {}", text);
        List<EventShortDto> eventShortDtoList = eventService.getEventsByLocationName(text);
        return new ResponseEntity<>(eventShortDtoList, HttpStatus.OK);
    }

    private SearchParametersPublicRequest checkParameters(String text, List<Long> categories, Boolean paid,
                                                          String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                          String sort, Integer from, Integer size) {
        LocalDateTime rangeStartTime = null;
        if (rangeStart != null) {
            rangeStartTime = LocalDateTime.parse(rangeStart, TIME_PATTERN);
        }
        LocalDateTime rangeEndTime = null;
        if (rangeEnd != null) {
            rangeEndTime = LocalDateTime.parse(rangeEnd, TIME_PATTERN);
        }
        if (rangeStartTime != null && rangeEndTime != null) {
            if (rangeEndTime.isBefore(rangeStartTime)) {
                throw new CustomValidationException("Incorrect parameters set",
                        String.format("rangeEnd cannot be earlier than rangeStart. rangeStart = %s, rangeEnd = %s", rangeStart, rangeEnd));
            }
        }
        SortOption sortOption = null;
        if (sort != null) {
            sortOption = SortOption.convert(sort.toUpperCase()).orElseThrow(() -> new IllegalArgumentException("Unknown state: " + text));
        }
        return new SearchParametersPublicRequest(text, categories, paid, rangeStartTime, rangeEndTime, onlyAvailable,
                sortOption, PageRequest.of(from / size, size));
    }


}
