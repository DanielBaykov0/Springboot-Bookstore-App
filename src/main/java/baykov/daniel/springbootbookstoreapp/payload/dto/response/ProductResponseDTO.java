package baykov.daniel.springbootbookstoreapp.payload.dto.response;

import baykov.daniel.springbootbookstoreapp.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ProductResponseDTO {

    @Schema(description = "Type of the product (e.g., BOOK, EBOOK)", example = "BOOK")
    private Product.ProductTypeEnum productType;

    @Schema(description = "Name of the product", example = "Sample Book")
    private String productName;

    @Schema(description = "Price of the product", example = "20.99")
    private BigDecimal productPrice;

    @Schema(description = "Quantity of the product", example = "2")
    private Integer quantity;
}
