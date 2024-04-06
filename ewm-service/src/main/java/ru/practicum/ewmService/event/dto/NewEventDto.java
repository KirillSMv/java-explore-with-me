package ru.practicum.ewmService.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmService.event.Location;
import ru.practicum.ewmService.event.validation.EventDateValidation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = "Минимальное количество символов в кратком описании - 20, " +
            "максимальное - 2000, пожалуйста, проверьте правильность указанных данных")
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank
    @Size(min = 20, max = 7000, message = "Минимальное количество символов в описании - 20, максимальное - 7000, " +
            "пожалуйста, проверьте правильность указанных данных")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    @EventDateValidation
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    @NotNull
    private Boolean paid;

    @Min(value = 0, message = "participantLimit cannot be negative")
    private Integer participantLimit; //0 - нет// ограничений на количество участников

    @NotNull
    private Boolean requestModeration;
    //Если true, то все заявки будут ожидать подтверждения инициатором события.
    // Если false - то будут подтверждаться автоматически.

    @NotBlank
    @Size(min = 3, max = 120, message = "Минимальное количество символов в заголовке - 3, максимальное - 120, " +
            "пожалуйста, проверьте правильность указанных данных")
    private String title;
}
