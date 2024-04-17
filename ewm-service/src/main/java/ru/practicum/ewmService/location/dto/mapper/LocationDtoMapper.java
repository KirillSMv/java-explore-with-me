package ru.practicum.ewmService.location.dto.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmService.event.dto.LocationShortDto;
import ru.practicum.ewmService.location.dto.LocationDto;
import ru.practicum.ewmService.location.model.Location;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationDtoMapper {
    public Location toLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setName(locationDto.getName());
        location.setLat(locationDto.getLat());
        location.setLon(locationDto.getLon());
        location.setRad(locationDto.getRad());
        return location;
    }

    public LocationShortDto toLocationShortDto(Location location) {
        return new LocationShortDto(location.getLat(), location.getLon());
    }

    public LocationDto toLocationDto(Location location) {
        return new LocationDto(location.getId(), location.getName(), location.getLat(), location.getLon(), location.getRad());
    }

    public List<LocationDto> toLocationDtoList(List<Location> locations) {
        return locations.stream().map(this::toLocationDto).collect(Collectors.toList());
    }
}
