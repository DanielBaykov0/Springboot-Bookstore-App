package baykov.daniel.springbootlibraryapp.validator.description;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DescriptionValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDescription {
    String message() default "Please enter a valid description.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
