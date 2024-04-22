package ru.practicum.ewmService.location.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LatCoordinatesValidator.class)
public @interface LatCoordinates {
    String message() default "Please check \"lat\" value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
