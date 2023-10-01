package baykov.daniel.springbootlibraryapp.payload.dto.request;

import baykov.daniel.springbootlibraryapp.validator.name.ValidName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class AuthorRequestDTO {

    @Schema(example = "John")
    @ValidName(message = "Author first name should not be null or empty and should have at least 1 English character.")
    private String firstName;

    @Schema(example = "Doe")
    @ValidName(message = "Author last name should not be null or empty and should have at least 1 English character.")
    private String lastName;

    @Schema(example = "1")
    private Long countryId;

    @Schema(example = "101")
    private Long cityId;

    @Schema(example = "1980-05-15")
    @Past(message = "Author birth date must be a past date.")
    private LocalDate birthDate;

    @Schema(example = "true")
    private Boolean isAlive;

    @Schema(example = "2022-08-25")
    private LocalDate deathDate;
}
