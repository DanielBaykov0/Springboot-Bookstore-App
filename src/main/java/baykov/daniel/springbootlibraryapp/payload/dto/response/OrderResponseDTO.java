package baykov.daniel.springbootlibraryapp.payload.dto.response;

import baykov.daniel.springbootlibraryapp.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderResponseDTO {

    @Schema(description = "Address of the user", example = "1234 Elm St, Apt 202, City, Country")
    private String address;

    @Schema(description = "Contact phone number for the order", example = "1234567890")
    private String contactPhoneNumber;

    @Schema(description = "Comment for the order", example = "Best store.")
    private String comment;

    @Schema(description = "Payment method for the order (e.g., CARD)", example = "CARD")
    private Order.PaymentMethod paymentMethod;
}
