package baykov.daniel.springbootbookstoreapp.payload.dto.response;

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
public class AudiobookResponseDTO {

    @Schema(description = "Title of the audiobook", example = "The Great Audiobook")
    private String title;

    @Schema(description = "Names of authors of the audiobook", example = "[John Doe, Jane Smith]")
    private List<String> authorNames;

    @Schema(description = "Names of categories the audiobook belongs to", example = "[Fiction, Mystery]")
    private List<String> categoryNames;

    @Schema(description = "ID of the narrator for the audiobook", example = "123")
    private Long narratorId;

    @Schema(description = "Language of the audiobook", example = "English")
    private String language;

    @Schema(description = "Publication year of the audiobook", example = "2021")
    private Integer publicationYear;

    @Schema(description = "Description of the audiobook", example = "An exciting audiobook adventure.")
    private String description;

    @Schema(description = "Duration of the audiobook in hours", example = "10.5")
    private BigDecimal duration;

    @Schema(description = "ISBN of the audiobook", example = "9781123456789")
    private String ISBN;

    @Schema(description = "File format of the audiobook", example = "MP3")
    private String fileFormat;

    @Schema(description = "File size of the audiobook", example = "50 MB")
    private String fileSize;

    @Schema(description = "Average rating of the audiobook", example = "4.5")
    private Double averageRating;

    @Schema(description = "Price of the audiobook", example = "20.99")
    private BigDecimal price;
}
