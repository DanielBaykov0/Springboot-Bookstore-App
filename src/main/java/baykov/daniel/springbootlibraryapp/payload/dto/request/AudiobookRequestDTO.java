package baykov.daniel.springbootlibraryapp.payload.dto.request;

import baykov.daniel.springbootlibraryapp.validator.bigdecimal.ValidBigDecimal;
import baykov.daniel.springbootlibraryapp.validator.description.ValidDescription;
import baykov.daniel.springbootlibraryapp.validator.file.ValidFileFormat;
import baykov.daniel.springbootlibraryapp.validator.file.ValidFileSize;
import baykov.daniel.springbootlibraryapp.validator.name.ValidName;
import baykov.daniel.springbootlibraryapp.validator.year.ValidPublicationYear;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
public class AudiobookRequestDTO {

    @Schema(example = "Book Title Example")
    @ValidName(message = "Audiobook title should not be null or empty and should have at least 1 English character.")
    private String title;

    @Schema(example = "[1, 2, 3]")
    private List<Long> authorIDs;

    @Schema(example = "[101, 102, 103]")
    private List<Long> categoryIDs;

    @Schema(example = "1")
    private Long narratorId;

    @Schema(example = "English")
    @ValidName(message = "Audiobook language should not be null or empty and should have at least 1 English character.")
    private String language;

    @Schema(example = "2022")
    @ValidPublicationYear
    private Integer publicationYear;

    @Schema(example = "This is a sample book description.")
    @ValidDescription(message = "Audiobook description should not be null or empty and should have at least 10 English characters.")
    private String description;

    @Schema(example = "10.5")
    @ValidBigDecimal(message = "Duration should not be null and should be at least 1.")
    private BigDecimal duration;

    @Schema(example = "978-316-148-410-0")
    @NotEmpty(message = "Book ISBN should not be null or empty")
    @org.hibernate.validator.constraints.ISBN(type = org.hibernate.validator.constraints.ISBN.Type.ISBN_13, message = "Book ISBN should be 13 digits long and have a valid format.")
    private String ISBN;

    @Schema(example = "PDF")
    @ValidFileFormat(message = "Audiobook file format should not be null or empty and should be a valid format.")
    private String fileFormat;

    @Schema(example = "2.5 MB")
    @ValidFileSize(message = "Audiobook file size should not be null or empty.")
    private String fileSize;

    @Schema(example = "25.99")
    @ValidBigDecimal(message = "Price should not be null and should be at least 1.")
    private BigDecimal price;
}
