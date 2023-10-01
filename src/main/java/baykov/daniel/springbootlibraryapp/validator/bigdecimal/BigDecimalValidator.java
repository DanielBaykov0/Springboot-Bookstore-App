package baykov.daniel.springbootlibraryapp.validator.bigdecimal;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class BigDecimalValidator implements ConstraintValidator<ValidBigDecimal, BigDecimal> {

    @Override
    public boolean isValid(BigDecimal duration, ConstraintValidatorContext context) {
        return duration != null && duration.compareTo(BigDecimal.ONE) >= 0;
    }
}
