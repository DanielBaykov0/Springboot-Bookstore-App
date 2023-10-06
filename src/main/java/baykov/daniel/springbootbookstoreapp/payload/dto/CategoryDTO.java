package baykov.daniel.springbootbookstoreapp.payload.dto;

import baykov.daniel.springbootbookstoreapp.validator.name.ValidName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryDTO {

    @Schema(description = "Name of the category", example = "Fiction")
    @ValidName(message = "Category should not be null or empty and should have at least 1 English character.")
    private String name;
}
