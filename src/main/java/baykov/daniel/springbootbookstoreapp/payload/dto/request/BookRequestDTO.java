package baykov.daniel.springbootbookstoreapp.payload.dto.request;

import baykov.daniel.springbootbookstoreapp.validator.bigdecimal.ValidBigDecimal;
import baykov.daniel.springbootbookstoreapp.validator.description.ValidDescription;
import baykov.daniel.springbootbookstoreapp.validator.name.ValidName;
import baykov.daniel.springbootbookstoreapp.validator.year.ValidPublicationYear;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
public class BookRequestDTO {

    @Schema(example = "Book Title")
    @ValidName(message = "Book title should not be null or empty and should have at least 1 English character.")
    private String title;

    @Schema(example = "[1, 2, 3]")
    private List<Long> authorIDs;

    @Schema(example = "[101, 102, 103]")
    private List<Long> categoryIDs;

    @Schema(example = "English")
    @ValidName(message = "Book language should not be null or empty and should have at least 1 English character.")
    private String language;

    @Schema(example = "2000")
    @ValidPublicationYear
    private Integer publicationYear;

    @Schema(example = "Description of the book...")
    @ValidDescription(message = "Book description should not be null or empty and should have at least 10 English characters.")
    private String description;

    @NotNull(message = "Book number of pages cannot be null")
    @Min(value = 1)
    @Schema(example = "300")
    private Integer numberOfPages;

    @Schema(example = "978-123-456-789-0")
    @NotEmpty(message = "Book ISBN should not be null or empty")
    @org.hibernate.validator.constraints.ISBN(type = org.hibernate.validator.constraints.ISBN.Type.ISBN_13, message = "Book ISBN should be 13 characters long and have a valid format.")
    private String ISBN;

    @NotNull(message = "Book available copies cannot be a negative number")
    @PositiveOrZero
    @Schema(example = "100")
    private Integer numberOfAvailableCopies;

    @NotNull(message = "Book total copies cannot be a negative number")
    @PositiveOrZero
    @Schema(example = "200")
    private Integer numberOfTotalCopies;

    @Schema(example = "20.99")
    @ValidBigDecimal(message = "Price should not be null and should be at least 1.")
    private BigDecimal price;
}
