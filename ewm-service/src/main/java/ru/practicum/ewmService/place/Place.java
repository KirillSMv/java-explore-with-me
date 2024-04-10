package ru.practicum.ewmService.place;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Place {
    @Id
    Long id;
    //ENUM type; //тип места (концертный зал, город, театр ...
    private String name;
    private Float lat;
    private Float lon;
    private Float radius;
}
