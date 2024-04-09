package ru.practicum.ewmService.event.validation;

import ru.practicum.ewmService.exceptions.InvalidEventDateException;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = EventDateValidator.class)

public @interface EventDateValidation {
    String message() default "Date of event cannot be less than 2 hours ahead of current time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends Throwable> exception() default InvalidEventDateException.class;
}
