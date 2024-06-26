package ru.practicum.ewmService.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    private Set<Long> events;

    private boolean pinned;

    @NotBlank
    @Size(min = 1, max = 50, message = "Минимальное количество символов в подборке событий - 1, " +
            "максимальное - 50, пожалуйста, проверьте правильность указанных данных")
    private String title;
}
