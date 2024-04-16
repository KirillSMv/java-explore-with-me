package ru.practicum.ewmService.location.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewmService.location.dto.LocationDto;

import java.util.List;

public interface LocationService {

    LocationDto addLocation(LocationDto locationDto);

    List<LocationDto> getLocations(Pageable pageable);

}
