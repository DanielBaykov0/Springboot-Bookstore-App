package baykov.daniel.springbootbookstoreapp.payload.dto.request;

import baykov.daniel.springbootbookstoreapp.validator.bigdecimal.ValidBigDecimal;
import baykov.daniel.springbootbookstoreapp.validator.description.ValidDescription;
import baykov.daniel.springbootbookstoreapp.validator.file.ValidFileFormat;
import baykov.daniel.springbootbookstoreapp.validator.file.ValidFileSize;
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
public class EbookRequestDTO {

    @Schema(description = "Title of the book", example = "The Great Gatsby")
    @ValidName(message = "Ebook title should not be null or empty and should have at least 1 English character.")
    private String title;

    @Schema(description = "IDs of authors associated with the book", example = "[1, 2, 3]")
    private List<Long> authorIDs;

    @Schema(description = "IDs of categories associated with the book", example = "[101, 102]")
    private List<Long> categoryIDs;

    @Schema(description = "Language of the book", example = "English")
    @ValidName(message = "Ebook language should not be null or empty and should have at least 1 English character.")
    private String language;

    @Schema(description = "Publication year of the book", example = "2020")
    @ValidPublicationYear
    private Integer publicationYear;

    @Schema(description = "Description of the book", example = "A captivating story of love and loss.")
    @ValidDescription(message = "Ebook description should not be null or empty and should have at least 10 English characters.")
    private String description;

    @Schema(description = "Number of pages in the book", example = "300")
    @NotNull(message = "Ebook number of pages cannot be null.")
    @Positive(message = "Ebook number of pages should be a positive number.")
    private Integer numberOfPages;

    @Schema(description = "ISBN of the book", example = "978-123-456-789-0")
    @NotEmpty(message = "Book ISBN should not be null or empty")
    @org.hibernate.validator.constraints.ISBN(type = org.hibernate.validator.constraints.ISBN.Type.ISBN_13, message = "Book ISBN should be 13 characters long and have a valid format.")
    private String ISBN;

    @Schema(description = "File format of the book", example = "PDF")
    @ValidFileFormat(message = "Ebook file format should not be null or empty and should be a valid format.")
    private String fileFormat;

    @Schema(description = "File size of the book", example = "5 MB")
    @ValidFileSize(message = "Ebook file size should not be null or empty.")
    private String fileSize;

    @Schema(description = "Price of the book", example = "19.99")
    @ValidBigDecimal(message = "Price should not be null and should be at least 1.")
    private BigDecimal price;
}
