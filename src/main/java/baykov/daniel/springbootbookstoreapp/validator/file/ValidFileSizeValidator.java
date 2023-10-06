package baykov.daniel.springbootbookstoreapp.validator.file;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidFileSizeValidator implements ConstraintValidator<ValidFileSize, String> {

    private static final Pattern FILE_SIZE_PATTERN = Pattern.compile("\\d+(\\.\\d+)?\\s*(MB|GB)");

    @Override
    public boolean isValid(String fileSize, ConstraintValidatorContext context) {
        if (fileSize == null || fileSize.trim().isEmpty()) {
            return false;
        }

        return FILE_SIZE_PATTERN.matcher(fileSize).matches();
    }
}
