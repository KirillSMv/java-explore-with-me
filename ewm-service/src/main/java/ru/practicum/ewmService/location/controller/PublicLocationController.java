package ru.practicum.ewmService.location.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmService.location.dto.LocationDto;
import ru.practicum.ewmService.location.service.LocationService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/location")
@Slf4j
@Validated
@RequiredArgsConstructor
public class PublicLocationController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<LocationDto>> getLocations(@RequestParam(value = "from", defaultValue = "0") @Min(value = 0) Integer from,
                                                          @RequestParam(value = "size", defaultValue = "10") @Min(value = 1) Integer size) {
        log.info("getLocations method, params: start = {}, size = {}", from, size);
        return new ResponseEntity<>(locationService.getLocations(PageRequest.of(from / size, size)), HttpStatus.OK);
    }
}
