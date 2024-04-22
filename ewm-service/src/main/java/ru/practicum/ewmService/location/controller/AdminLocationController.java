package ru.practicum.ewmService.location.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmService.location.dto.LocationDto;
import ru.practicum.ewmService.location.service.LocationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/location")
@Slf4j
@RequiredArgsConstructor
public class AdminLocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<LocationDto> addLocation(@RequestBody @Valid LocationDto locationDto) {
        log.info("addLocation method, location = {}", locationDto);
        return new ResponseEntity<>(locationService.addLocation(locationDto), HttpStatus.CREATED);
    }
}
