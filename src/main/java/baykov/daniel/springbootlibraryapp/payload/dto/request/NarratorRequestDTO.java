package baykov.daniel.springbootlibraryapp.payload.dto.request;

import baykov.daniel.springbootlibraryapp.validator.description.ValidDescription;
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
public class NarratorRequestDTO {

    @Schema(description = "First name of the narrator", example = "Emily")
    @ValidName(message = "Narrator first name should not be null or empty and should have at least 1 English character.")
    private String firstName;

    @Schema(description = "Last name of the narrator", example = "Johnson")
    @ValidName(message = "Narrator last name should not be null or empty and should have at least 1 English character.")
    private String lastName;

    @Schema(description = "Biography of the narrator", example = "Experienced narrator with a soothing voice.")
    @ValidDescription(message = "Narrator biography should not be null or empty and should have at least 10 English characters.")
    private String biography;

    @Schema(description = "ID of the country associated with the narrator", example = "1")
    private Long countryId;

    @Schema(description = "ID of the city associated with the narrator", example = "101")
    private Long cityId;

    @Past
    @Schema(description = "Birth date of the narrator", example = "1975-08-22")
    private LocalDate birthDate;

    @Schema(description = "Flag indicating if the narrator is alive", example = "true")
    private Boolean isAlive;

    @Schema(description = "Death date of the narrator", example = "2020-11-10")
    private LocalDate deathDate;
}
