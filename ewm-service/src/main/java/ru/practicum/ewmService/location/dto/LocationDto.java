package ru.practicum.ewmService.location.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmService.location.validation.LatCoordinates;
import ru.practicum.ewmService.location.validation.LonCoordinates;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    private Long id;

    @NotBlank(message = "name should not be blank")
    @Size(min = 4, max = 120, message = "name cannot has fewer characters than 4 and more than 120")
    private String name;

    @NotNull(message = "please add \"lat\" value")
    @LatCoordinates
    private Float lat;

    @NotNull(message = "please add \"lon\" value")
    @LonCoordinates
    private Float lon;

    @NotNull(message = "please add \"rad\" value")
    private Float rad;

    @AssertTrue(message = "Please check provided coordinates")
    boolean areCoordinatesValid() {
/*        if (lat < -90.0f || lat > 90.0f) {
            throw new IllegalArgumentException("\"lat\" value cannot be fewer than -90 and more than 90");
        }
        if (lon < -180.0f || lon > 180.0f) {
            throw new IllegalArgumentException("\"lon\" value cannot be fewer than -180 and more than 180");
        }*/
        return lat >= -90f && lat <= 90f && lon >= -180f && lon <= 180f;
    }
}
