package ru.practicum.ewmService.location.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LonCoordinatesValidator implements ConstraintValidator<LonCoordinates, Float> {
    @Override
    public boolean isValid(Float value, ConstraintValidatorContext constraintValidatorContext) {
        return value >= -180.0f && value <= 180.0f;
    }
}
