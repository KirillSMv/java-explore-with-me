package ru.practicum.ewmService.event.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotBlankNullableValidator implements ConstraintValidator<NotBlankNullable, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        return !value.isBlank();
    }
}
