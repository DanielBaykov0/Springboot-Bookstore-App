package baykov.daniel.springbootlibraryapp.payload.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import static baykov.daniel.springbootlibraryapp.constant.AppConstants.CURRENT_YEAR;

@Getter
@Setter
public class BookDTO {

    @NotEmpty(message = "Book title should not be null or empty")
    @Size(min = 2, message = "Book title should have at least 2 characters")
    private String title;

    @NotNull(message = "Author id cannot be null")
    private Long authorId;

    @NotEmpty(message = "Book genre should not be null or empty")
    @Size(min = 4, message = "Book genre should have at least 4 characters")
    private String genre;

    @NotNull(message = "Book publication year cannot be null")
    @Min(value = 1400, message = "Book publication year should be between 1400 and 2023")
    @Max(value = CURRENT_YEAR)
    private Integer publicationYear;

    @NotEmpty(message = "Book description should not be null or empty")
    @Size(min = 10, message = "Book description should have at least 10 characters")
    private String description;

    @NotEmpty(message = "Book ISBN should not be null or empty")
    @Size(min = 13, max = 13, message = "Book ISBN should be 13 characters long")
    private String ISBN;

    @NotNull(message = "Book available copies cannot be a negative number")
    @PositiveOrZero
    private Integer numberOfAvailableCopies;

    @NotNull(message = "Book total copies cannot be a negative number")
    @PositiveOrZero
    private Integer numberOfTotalCopies;

    @URL
    private String readingLink;

    @URL
    private String downloadLink;
}
