package ru.practicum.ewmService.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequest {
    //Изменение информации о подборке событий. Если поле в запросе не указано (равно null) - значит изменение этих данных не треубется.
    List<Long> events;
    Boolean pinned;
    @NotBlank
    @Size(min = 1, max = 50, message = "Минимальное количество символов в подборке событий - 1, " +
            "максимальное - 50, пожалуйста, проверьте правильность указанных данных")
    String title;
}
