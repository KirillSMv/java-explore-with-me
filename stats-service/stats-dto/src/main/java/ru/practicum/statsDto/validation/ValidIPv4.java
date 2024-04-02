package ru.practicum.statsDto.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@Pattern(regexp = ValidIPv4.IPV4_REGEX)
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface ValidIPv4 {
    String IPV4_REGEX = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";

    String message() default "ip адрес должен соответствовать: " + IPV4_REGEX;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
