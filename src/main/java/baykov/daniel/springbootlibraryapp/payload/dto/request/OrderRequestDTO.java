package baykov.daniel.springbootlibraryapp.payload.dto.request;

import baykov.daniel.springbootlibraryapp.entity.Order;
import baykov.daniel.springbootlibraryapp.validator.description.ValidDescription;
import baykov.daniel.springbootlibraryapp.validator.enums.EnumValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderRequestDTO {

    @Schema(description = "Comment for the order", example = "Good service.")
    @ValidDescription(message = "Order comment should not be null or empty and should have at least 10 English characters.")
    private String comment;

    @Schema(description = "Payment method for the order", allowableValues = { "CARD" })
    @NotNull(message = "Payment method is required.")
    @EnumValid(enumClass = Order.PaymentMethod.class)
    private Order.PaymentMethod paymentMethod;
}
