package ru.practicum.ewmService.event.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
public class Location {
    @Column(name = "lat")
    @Size(min = -90, max = 90)
    private Float lat;

    @Column(name = "lon")
    @Size(min = -180, max = 180)
    private Float lon;

    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
