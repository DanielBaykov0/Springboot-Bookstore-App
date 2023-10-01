package baykov.daniel.springbootlibraryapp.payload.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BookResponseDTO {

    @Schema(description = "Title of the book", example = "Example Book Title")
    private String title;

    @Schema(description = "Names of authors", example = "[John Doe, Jane Smith]")
    private List<String> authorNames;

    @Schema(description = "Names of categories", example = "[Fiction, Mystery]")
    private List<String> categoryNames;

    @Schema(description = "Language of the book", example = "English")
    private String language;

    @Schema(description = "Year of publication", example = "2021")
    private Integer publicationYear;

    @Schema(description = "Description of the book", example = "This is a fascinating book about...")
    private String description;

    @Schema(description = "Number of pages in the book", example = "350")
    private Integer numberOfPages;

    @Schema(description = "ISBN of the book", example = "9783161484100")
    private String ISBN;

    @Schema(description = "Number of available copies", example = "10")
    private Integer numberOfAvailableCopies;

    @Schema(description = "Total number of copies", example = "100")
    private Integer numberOfTotalCopies;

    @Schema(description = "Average rating of the book", example = "4.5")
    private Double averageRating;

    @Schema(description = "Price of the book", example = "25.99")
    private BigDecimal price;
}
