package ru.practicum.ewmService.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    //Широта и долгота места проведения события
    Long id; //?
    Float lat;
    Float lon;
}
