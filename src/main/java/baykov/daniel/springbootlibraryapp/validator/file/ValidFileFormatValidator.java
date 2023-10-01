package baykov.daniel.springbootlibraryapp.validator.file;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class ValidFileFormatValidator implements ConstraintValidator<ValidFileFormat, String> {

    private final List<String> validFileFormats =
            Arrays.asList("PDF", "DOC", "TXT", "EPUB", "MOBI", "AZW", "HTML", "MP3", "AAC", "M4B", "WAV", "FLAC", "OGG");

    @Override
    public boolean isValid(String fileFormat, ConstraintValidatorContext context) {
        if (fileFormat == null || fileFormat.trim().isEmpty()) {
            return false;
        }

        return validFileFormats.contains(fileFormat.toUpperCase());
    }
}
