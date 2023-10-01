package baykov.daniel.springbootlibraryapp.validator.year;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class PublicationYearValidator implements ConstraintValidator<ValidPublicationYear, Integer> {

    @Override
    public boolean isValid(Integer publicationYear, ConstraintValidatorContext context) {
        if (publicationYear == null) {
            return false;
        }

        int currentYear = Year.now().getValue();

        return publicationYear >= 1400 && publicationYear <= currentYear;
    }
}
