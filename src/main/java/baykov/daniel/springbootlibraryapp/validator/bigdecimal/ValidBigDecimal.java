package baykov.daniel.springbootlibraryapp.validator.bigdecimal;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BigDecimalValidator.class)
@Documented
public @interface ValidBigDecimal {
    String message() default "Please enter a valid value.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
