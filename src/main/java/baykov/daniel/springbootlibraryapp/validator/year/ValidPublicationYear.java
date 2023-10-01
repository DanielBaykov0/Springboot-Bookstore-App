package baykov.daniel.springbootlibraryapp.validator.year;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PublicationYearValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPublicationYear {
    String message() default "Book publication year should be between 1400 and {value}.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
