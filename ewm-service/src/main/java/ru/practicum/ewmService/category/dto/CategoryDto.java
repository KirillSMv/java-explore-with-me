package ru.practicum.ewmService.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
