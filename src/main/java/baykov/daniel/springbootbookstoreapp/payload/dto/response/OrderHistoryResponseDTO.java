package baykov.daniel.springbootbookstoreapp.payload.dto.response;

import baykov.daniel.springbootbookstoreapp.entity.Order;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderHistoryResponseDTO {

    @Schema(description = "ID of the order", example = "12345")
    private Long orderId;

    @Schema(description = "Comment for the order", example = "Great service.")
    private String comment;

    @Schema(description = "Contact phone number associated with the order", example = "0887 08 08 08")
    private String contactPhoneNumber;

    @Schema(description = "Date and time when the order was created", example = "2023-09-30T14:15:00")
    private LocalDateTime createdOn;

    @Schema(description = "Date and time when the order was delivered", example = "2023-10-05T11:30:00")
    private LocalDateTime deliveredOn;

    @Schema(description = "Price of the order", example = "50.99")
    private BigDecimal price;

    @Schema(description = "Status of the order (e.g., PENDING, DELIVERED)", example = "COMPLETED")
    private Order.OrderStatusEnum status;
}
