package ru.practicum.ewmService.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
@Data
@NoArgsConstructor
public class Location {
    @Column(name = "lat")
    @Size(min = -90, max = 90)
    private Float lat;

    @Column(name = "lon")
    @Size(min = -180, max = 180)
    private Float lon;
}
