package baykov.daniel.springbootlibraryapp.payload.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
public class CartResponseDTO {

    @Schema(description = "List of products in the cart")
    private List<ProductResponseDTO> products;

    @Schema(description = "Count of products in the cart", example = "5")
    private Long productsCount;

    @Schema(description = "Total sum of the products in the cart", example = "123.45")
    private BigDecimal productsSum;
}
