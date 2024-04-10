package ru.practicum.ewmService.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmService.event.enums.EventStateAction;
import ru.practicum.ewmService.event.model.Location;
import ru.practicum.ewmService.event.validation.EventDateValidation;
import ru.practicum.ewmService.event.validation.NotBlankNullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest {
    @NotBlankNullable
    @Size(min = 20, max = 2000, message = "Минимальное количество символов в кратком описании - 20, " +
            "максимальное - 2000, пожалуйста, проверьте правильность указанных данных")
    private String annotation;

    private Long category;

    @NotBlankNullable
    @Size(min = 20, max = 7000, message = "Минимальное количество символов в описании - 20, максимальное - 7000, " +
            "пожалуйста, проверьте правильность указанных данных")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @EventDateValidation
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    @Min(value = 0, message = "participantLimit cannot be negative")
    private Integer participantLimit; //0 - нет// ограничений на количество участников

    private Boolean requestModeration;
    //Если true, то все заявки будут ожидать подтверждения инициатором события.
    // Если false - то будут подтверждаться автоматически.

    EventStateAction stateAction = EventStateAction.SEND_TO_REVIEW;

    @NotBlankNullable
    @Size(min = 3, max = 120, message = "Минимальное количество символов в заголовке - 3, максимальное - 120, " +
            "пожалуйста, проверьте правильность указанных данных")
    private String title;
}
