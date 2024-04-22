package ru.practicum.ewmService.location.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmService.location.dto.LocationDto;
import ru.practicum.ewmService.location.dto.mapper.LocationDtoMapper;
import ru.practicum.ewmService.location.model.Location;
import ru.practicum.ewmService.location.storage.LocationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationDtoMapper locationDtoMapper;

    @Override
    public LocationDto addLocation(LocationDto locationDto) {
        Location location = locationDtoMapper.toLocation(locationDto);
        return locationDtoMapper.toLocationDto(locationRepository.save(location));
    }

    @Override
    public List<LocationDto> getLocations(Pageable pageable) {
        List<Location> locations = locationRepository.findAll(pageable).getContent();
        return locationDtoMapper.toLocationDtoList(locations);
    }
}
