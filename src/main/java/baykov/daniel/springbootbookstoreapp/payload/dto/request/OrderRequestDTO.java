package baykov.daniel.springbootbookstoreapp.payload.dto.request;

import baykov.daniel.springbootbookstoreapp.entity.Order;
import baykov.daniel.springbootbookstoreapp.validator.description.ValidDescription;
import baykov.daniel.springbootbookstoreapp.validator.enums.PaymentMethodEnumValid;
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

    @Schema(description = "Payment method for the order", allowableValues = {"CARD"})
    @NotNull(message = "Payment method is required.")
    @PaymentMethodEnumValid(anyOf = {
            Order.PaymentMethod.CARD
    })
    private Order.PaymentMethod paymentMethod;
}
