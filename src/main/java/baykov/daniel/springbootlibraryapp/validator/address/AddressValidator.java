package baykov.daniel.springbootlibraryapp.validator.address;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class AddressValidator implements ConstraintValidator<ValidAddress, String> {

    private static final String ADDRESS_REGEX = "^[0-9A-Za-z\\s,-]+$";

    @Override
    public boolean isValid(String address, ConstraintValidatorContext context) {
        if (address == null || address.isEmpty()) {
            return false;
        }

        return Pattern.matches(ADDRESS_REGEX, address);
    }
}