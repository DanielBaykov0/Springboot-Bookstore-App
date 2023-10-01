package baykov.daniel.springbootlibraryapp.payload.dto;

import baykov.daniel.springbootlibraryapp.validator.name.ValidName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CityDTO {

    @Schema(description = "Name of the city", example = "New York")
    @ValidName(message = "City should not be null or empty and should have at least 1 English character.")
    private String name;

    @Schema(description = "ID of the country to which the city belongs", example = "1")
    private Long countryId;
}
