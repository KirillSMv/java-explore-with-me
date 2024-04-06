package ru.practicum.ewmService.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank
    @Size(min = 6, max = 254, message = "Минимальное количество символов в электронной почте пользователя - 6, " +
            "максимальное - 254, пожалуйста, проверьте правильность указанных данных")
    private String name;

    @Email
    @Size(min = 2, max = 250, message = "Минимальное количество символов имени пользователя - 2, " +
            "максимальное - 250, пожалуйста, проверьте правильность указанных данных")
    private String email;

}
