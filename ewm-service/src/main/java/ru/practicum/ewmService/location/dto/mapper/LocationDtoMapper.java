package ru.practicum.ewmService.location.dto.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmService.location.dto.LocationDto;
import ru.practicum.ewmService.location.model.EventLocation;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationDtoMapper {

    public EventLocation toLocation(LocationDto locationDto) {
        EventLocation eventLocation = new EventLocation();
        eventLocation.setName(locationDto.getName());
        eventLocation.setLat(locationDto.getLat());
        eventLocation.setLon(locationDto.getLon());
        eventLocation.setRad(locationDto.getRad());
        return eventLocation;
    }

    public LocationDto toLocationDto(EventLocation eventLocation) {
        return new LocationDto(eventLocation.getId(), eventLocation.getName(), eventLocation.getLat(), eventLocation.getLon(), eventLocation.getRad());
    }

    public List<LocationDto> toLocationDtoList(List<EventLocation> eventLocations) {
        return eventLocations.stream().map(this::toLocationDto).collect(Collectors.toList());
    }
}
