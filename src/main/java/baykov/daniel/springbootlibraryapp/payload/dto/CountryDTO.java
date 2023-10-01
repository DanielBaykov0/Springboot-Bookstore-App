package baykov.daniel.springbootlibraryapp.payload.dto;

import baykov.daniel.springbootlibraryapp.validator.name.ValidName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CountryDTO {

    @Schema(description = "Name of the country", example = "United States")
    @ValidName(message = "Country should not be null or empty and should have at least 1 English character.")
    private String name;
}
