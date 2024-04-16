package ru.practicum.ewmService.location.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LatCoordinatesValidator implements ConstraintValidator<LatCoordinates, Float> {

    @Override
    public boolean isValid(Float value, ConstraintValidatorContext constraintValidatorContext) {
        return value >= -90.0f && value <= 90.0f;
    }
}
