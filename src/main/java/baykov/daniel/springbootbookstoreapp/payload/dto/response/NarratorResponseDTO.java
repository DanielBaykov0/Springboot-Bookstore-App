package baykov.daniel.springbootbookstoreapp.payload.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NarratorResponseDTO {

    @Schema(description = "First name of the narrator", example = "John")
    private String firstName;

    @Schema(description = "Last name of the narrator", example = "Doe")
    private String lastName;

    @Schema(description = "Biography of the narrator", example = "Experienced narrator with a passion for storytelling.")
    private String biography;

    @Schema(description = "ID of the country where the narrator is from", example = "1")
    private Long countryId;

    @Schema(description = "ID of the city where the narrator is from", example = "101")
    private Long cityId;

    @Schema(description = "Date of birth of the narrator", example = "1990-05-15")
    private LocalDate birthDate;

    @Schema(description = "Flag indicating whether the narrator is alive", example = "true")
    private Boolean isAlive;

    @Schema(description = "Date of death of the narrator if applicable", example = "2022-02-28")
    private LocalDate deathDate;
}
