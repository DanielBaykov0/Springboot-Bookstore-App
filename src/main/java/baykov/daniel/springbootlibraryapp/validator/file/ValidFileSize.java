package baykov.daniel.springbootlibraryapp.validator.file;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidFileSizeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFileSize {
    String message() default "Invalid file size";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
