package ru.practicum.ewmService.event.validation;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = EventDateValidator.class)
public @interface EventDateAdminValidation {
}
