package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.config.RequestData;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.OrderRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.OrderResponseDTO;
import baykov.daniel.springbootbookstoreapp.service.OrderService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/orders")
@Tag(name = "REST APIs for Order Resource")
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Make Order REST API",
            description = "Make Order REST API is used to make an order"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    @PostMapping
    public ResponseEntity<OrderResponseDTO> prepareOrderRequest(
            @Valid @RequestBody OrderRequestDTO orderRequestDTO, Authentication authentication) throws StripeException {
        log.info("Correlation ID: {}. Order placement initiated by user: {}", RequestData.getCorrelationId(), authentication.getName());

        OrderResponseDTO response = orderService.placeOrder(orderRequestDTO, authentication);

        log.info("Correlation ID: {}. Order successfully placed for user: {}", RequestData.getCorrelationId(), authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
