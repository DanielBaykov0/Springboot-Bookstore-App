package baykov.daniel.springbootlibraryapp.validator.description;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DescriptionValidator implements ConstraintValidator<ValidDescription, String> {

    @Override
    public boolean isValid(String description, ConstraintValidatorContext context) {
        if (description == null || description.trim().isEmpty()) {
            return false;
        }

        return description.length() >= 10;
    }
}
