package baykov.daniel.springbootlibraryapp.validator.file;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidFileFormatValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFileFormat {
    String message() default "Invalid file format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
