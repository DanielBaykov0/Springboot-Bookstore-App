package baykov.daniel.springbootlibraryapp.validator.name;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidNameValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidName {
    String message() default "Please enter a valid name.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
