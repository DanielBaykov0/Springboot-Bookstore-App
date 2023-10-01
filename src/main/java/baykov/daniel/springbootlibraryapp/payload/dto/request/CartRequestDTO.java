package baykov.daniel.springbootlibraryapp.payload.dto.request;

import baykov.daniel.springbootlibraryapp.entity.Product;
import baykov.daniel.springbootlibraryapp.validator.enums.EnumValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartRequestDTO {

    @Schema(example = "123")
    @NotNull(message = "Product ID should not be null.")
    @Positive(message = "Product ID should be a positive number.")
    private Long productId;

    @Schema(example = "BOOK")
    @EnumValid(enumClass = Product.ProductTypeEnum.class)
    private Product.ProductTypeEnum productType;

    @Schema(example = "5")
    @NotNull(message = "Quantity should not be null.")
    @Positive(message = "Quantity should be a positive .")
    private Integer quantity;
}
