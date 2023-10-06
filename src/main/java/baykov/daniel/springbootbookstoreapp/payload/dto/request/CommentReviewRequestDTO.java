package baykov.daniel.springbootbookstoreapp.payload.dto.request;

import baykov.daniel.springbootbookstoreapp.entity.Product;
import baykov.daniel.springbootbookstoreapp.validator.enums.ProductTypeEnumValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentReviewRequestDTO {

    @Schema(description = "ID of the product", example = "123")
    @NotNull(message = "Product ID should not be null.")
    @Positive(message = "Product ID should be a positive number.")
    private Long productId;

    @Schema(description = "Type of the product", example = "BOOK")
    @NotNull(message = "Product type should not be null.")
    @ProductTypeEnumValid(anyOf = {
            Product.ProductTypeEnum.BOOK, Product.ProductTypeEnum.AUDIOBOOK, Product.ProductTypeEnum.EBOOK
    })
    private Product.ProductTypeEnum productType;

    @Schema(description = "Comment text", example = "This is a great product!")
    @Size(min = 3, max = 500, message = "Comment should be min 3 and max 500 characters long.")
    private String comment;

    @Schema(description = "Rating for the product", example = "5")
    @Min(value = 1, message = "Rating should be at least 1")
    @Max(value = 5, message = "Rating should be at most 5")
    @NotNull(message = "Rating should not be null")
    private Integer rating;
}
