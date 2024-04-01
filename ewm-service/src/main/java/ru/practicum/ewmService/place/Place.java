package ru.practicum.ewmService.place;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Place {
    Long id;
    //ENUM type; //тип места (концертный зал, город, театр ...
    String name;
    Float lat;
    Float lon;
    Float radius;
}
