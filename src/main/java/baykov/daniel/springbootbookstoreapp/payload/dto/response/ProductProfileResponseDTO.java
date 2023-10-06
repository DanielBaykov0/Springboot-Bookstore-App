package baykov.daniel.springbootbookstoreapp.payload.dto.response;

import baykov.daniel.springbootbookstoreapp.entity.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductProfileResponseDTO {

    @Schema(description = "Title of the product", example = "Sample Book")
    private String title;

    @Schema(description = "Language of the product", example = "English")
    private String language;

    @Schema(description = "Number of pages of the product", example = "300")
    private Integer numberOfPages;

    @Schema(description = "Publication year of the product", example = "2021")
    private Integer publicationYear;

    @Schema(description = "Description of the product", example = "This is a sample book.")
    private String description;

    @Schema(description = "ISBN of the product", example = "978-316-148-410-0")
    private String ISBN;

    @Schema(description = "Average rating of the product", example = "4.5")
    private double averageRating;

    @Schema(description = "Type of the product (e.g., BOOK, EBOOK)", example = "BOOK")
    private Product.ProductTypeEnum productType;

    @Schema(description = "Duration of the product (applicable for audiobooks)", example = "5.5")
    private BigDecimal duration;

    @Schema(description = "File format of the product", example = "PDF")
    private String fileFormat;

    @Schema(description = "File size of the product", example = "2.5MB")
    private String fileSize;

    @Schema(description = "Name of the author of the product", example = "John Doe")
    private String authorName;

    @Schema(description = "Category of the product", example = "Fiction")
    private String category;
}
