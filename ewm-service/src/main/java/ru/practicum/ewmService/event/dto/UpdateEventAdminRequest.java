package ru.practicum.ewmService.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmService.event.enums.EventStateAction;
import ru.practicum.ewmService.event.validation.EventDateValidation;
import ru.practicum.ewmService.event.validation.NotBlankNullable;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventAdminRequest {
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
    LocalDateTime eventDate;

    private LocationShortDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private EventStateAction stateAction;

    @NotBlankNullable
    @Size(min = 3, max = 120, message = "Минимальное количество символов в заголовке - 3, максимальное - 120, " +
            "пожалуйста, проверьте правильность указанных данных")
    private String title;
}
