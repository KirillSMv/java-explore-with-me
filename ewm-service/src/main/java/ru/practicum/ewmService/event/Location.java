package ru.practicum.ewmService.event;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
public class Location {
    @Column(name = "lat")
    private Float lat;
    @Column(name = "lon")
    private Float lon;
}
