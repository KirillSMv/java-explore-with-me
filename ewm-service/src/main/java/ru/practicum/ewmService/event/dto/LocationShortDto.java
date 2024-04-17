package ru.practicum.ewmService.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewmService.location.validation.LatCoordinates;
import ru.practicum.ewmService.location.validation.LonCoordinates;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocationShortDto {
    @Column(name = "lat")
    @LatCoordinates
    private Float lat;

    @Column(name = "lon")
    @LonCoordinates
    private Float lon;

    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
