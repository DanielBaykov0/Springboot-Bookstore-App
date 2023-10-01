package baykov.daniel.springbootlibraryapp.payload.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentReviewResponseDTO {

    @Schema(description = "Username of the user who posted the comment", example = "john_doe")
    private String userName;

    @Schema(description = "The comment posted by the user", example = "This is a great product!")
    private String comment;

    @Schema(description = "The rating given by the user", example = "4")
    private Integer rating;

    @Schema(description = "The average rating for the product", example = "4.2")
    private Double averageRating;

    @Schema(description = "The timestamp when the comment was posted", example = "2023-09-29 15:45:00")
    private String postedAt;

    @Schema(description = "The timestamp when the comment was last updated", example = "2023-09-30 09:12:00")
    private String updatedAt;
}
