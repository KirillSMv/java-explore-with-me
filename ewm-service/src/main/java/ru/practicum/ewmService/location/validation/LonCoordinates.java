package ru.practicum.ewmService.location.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LonCoordinatesValidator.class)
public @interface LonCoordinates {
    String message() default "Please check \"lon\" value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
