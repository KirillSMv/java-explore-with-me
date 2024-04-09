package ru.practicum.ewmService.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmService.event.validation.NotBlankNullable;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequest {
    private Set<Long> events;

    private Boolean pinned;

    @NotBlankNullable
    @Size(min = 1, max = 50, message = "Минимальное количество символов в подборке событий - 1, " +
            "максимальное - 50, пожалуйста, проверьте правильность указанных данных")
    private String title;
}
