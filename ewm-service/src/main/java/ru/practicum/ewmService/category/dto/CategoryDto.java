package ru.practicum.ewmService.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;

    @NotBlank
    @Size(min = 1, max = 50, message = "Минимальное количество символов в названии категории - 1, " +
            "максимальное - 50, пожалуйста, проверьте правильность указанных данных")
    private String name;
}
