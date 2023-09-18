package baykov.daniel.springbootlibraryapp.payload.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import static baykov.daniel.springbootlibraryapp.constant.AppConstants.CURRENT_YEAR;

@Getter
@Setter
public class AudiobookDTO {

    @NotEmpty(message = "Book title should not be null or empty")
    @Size(min = 2, message = "Book title should have at least 2 characters")
    private String title;

    @NotNull(message = "Author should not be null")
    private Long authorId;

    @NotNull(message = "Narrator should not be null")
    private Long narratorId;

    @NotNull(message = "Book category should not be null")
    private Long categoryId;

    @NotEmpty(message = "Book language should not be null or empty")
    @Size(min = 2, message = "Book language should have at least 2 characters")
    private String language;

    @NotNull(message = "Book publication year cannot be null")
    @Min(value = 1400, message = "Book publication year should be between 1400 and 2023")
    @Max(value = CURRENT_YEAR)
    private Integer publicationYear;

    @NotEmpty(message = "Book description should not be null or empty")
    @Size(min = 10, message = "Book description should have at least 10 characters")
    private String description;

    @NotNull(message = "Book duration cannot be null")
    @Min(value = 1)
    private BigDecimal duration;

    @NotEmpty(message = "Book ISBN should not be null or empty")
    @Size(min = 13, max = 13, message = "Book ISBN should be 13 characters long")
    private String ISBN;

    private Boolean isDownloadable;

    @NotEmpty(message = "Book file format should not be null or empty")
    @Size(min = 2, message = "Book file format should be at least 2 characters long")
    private String fileFormat;

    @NotEmpty(message = "Book file size should not be null or empty")
    @Size(min = 4, message = "Book file size should be at least 4 characters long")
    private String fileSize;

    @NotNull(message = "Book price cannot be null")
    @Min(value = 1)
    private BigDecimal price;
}
