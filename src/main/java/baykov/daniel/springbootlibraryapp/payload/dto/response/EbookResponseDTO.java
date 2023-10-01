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
public class EbookResponseDTO {

    @Schema(description = "Title of the ebook", example = "Introduction to Programming")
    private String title;

    @Schema(description = "List of author names for the ebook", example = "[John Doe, Jane Smith]")
    private List<String> authorNames;

    @Schema(description = "List of category names for the ebook", example = "[Programming, Technology]")
    private List<String> categoryNames;

    @Schema(description = "Language of the ebook", example = "English")
    private String language;

    @Schema(description = "Publication year of the ebook", example = "2022")
    private Integer publicationYear;

    @Schema(description = "Description of the ebook", example = "This ebook provides an introduction to programming concepts.")
    private String description;

    @Schema(description = "Number of pages in the ebook", example = "250")
    private Integer numberOfPages;

    @Schema(description = "ISBN of the ebook", example = "9783161484100")
    private String ISBN;

    @Schema(description = "File format of the ebook", example = "PDF")
    private String fileFormat;

    @Schema(description = "File size of the ebook", example = "5 MB")
    private String fileSize;

    @Schema(description = "Average rating for the ebook", example = "4.5")
    private Double averageRating;

    @Schema(description = "Price of the ebook", example = "25.99")
    private BigDecimal price;
}
