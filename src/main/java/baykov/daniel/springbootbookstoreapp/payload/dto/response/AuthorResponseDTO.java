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
public class AuthorResponseDTO {

    @Schema(description = "First name of the author", example = "John")
    private String firstName;

    @Schema(description = "Last name of the author", example = "Doe")
    private String lastName;

    @Schema(description = "ID of the country the author is from", example = "123")
    private Long countryId;

    @Schema(description = "ID of the city the author is associated with", example = "456")
    private Long cityId;

    @Schema(description = "Birth date of the author", example = "1980-01-15")
    private LocalDate birthDate;

    @Schema(description = "Flag indicating if the author is alive", example = "true")
    private Boolean isAlive;

    @Schema(description = "Date of death of the author", example = "2022-05-20")
    private LocalDate deathDate;
}
