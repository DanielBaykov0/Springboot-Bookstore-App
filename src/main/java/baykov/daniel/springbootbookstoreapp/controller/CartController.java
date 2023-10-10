package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.config.RequestData;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.CartRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.CartResponseDTO;
import baykov.daniel.springbootbookstoreapp.service.CartService;
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
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/cart")
@Tag(name = "REST APIs for Cart Resource")
public class CartController {

    private final CartService cartService;

    @Operation(
            summary = "Add To Cart REST API",
            description = "Add To Cart REST API is used to add products to the cart"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addToCart(@Valid @RequestBody CartRequestDTO cartRequestDTO, Authentication authentication) {
        log.info("Correlation ID: {}. Received request to add to cart. User: {}, Product ID: {}, Product Type: {}, Quantity: {}",
                RequestData.getCorrelationId(), authentication.getName(), cartRequestDTO.getProductId(), cartRequestDTO.getProductType(), cartRequestDTO.getQuantity());

        CartResponseDTO cartResponseDTO = cartService.addToCart(authentication, cartRequestDTO);

        log.info("Correlation ID: {}. Product added to cart successfully. User: {}, Product ID: {}, Product Type: {}, Quantity: {}",
                RequestData.getCorrelationId(), authentication.getName(), cartRequestDTO.getProductId(), cartRequestDTO.getProductType(), cartRequestDTO.getQuantity());

        return new ResponseEntity<>(cartResponseDTO, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Remove From Cart REST API",
            description = "Remove From Cart REST API is used to remove products from the cart"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    @PostMapping("/remove")
    public ResponseEntity<CartResponseDTO> removeFromCart(@Valid @RequestBody CartRequestDTO cartRequestDTO, Authentication authentication) {
        log.info("Correlation ID: {}. Received request to remove from cart. User: {}, Product ID: {}, Product Type: {}, Quantity: {}",
                RequestData.getCorrelationId(), authentication.getName(), cartRequestDTO.getProductId(), cartRequestDTO.getProductType(), cartRequestDTO.getQuantity());

        CartResponseDTO cartResponseDTO = cartService.removeFromCart(authentication, cartRequestDTO);

        log.info("Correlation ID: {}. Product removed from cart successfully. User: {}, Product ID: {}, Product Type: {}, Quantity: {}",
                RequestData.getCorrelationId(), authentication.getName(), cartRequestDTO.getProductId(), cartRequestDTO.getProductType(), cartRequestDTO.getQuantity());

        return ResponseEntity.ok(cartResponseDTO);
    }

    @Operation(
            summary = "Get User Cart REST API",
            description = "Get User Cart REST API is used to show the items in the cart of a user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    @GetMapping
    public ResponseEntity<CartResponseDTO> showUserCart(Authentication authentication) {
        log.info("Correlation ID: {}. Received request to show user cart. User: {}", RequestData.getCorrelationId(), authentication.getName());

        CartResponseDTO cartResponseDTO = cartService.getUserCart(authentication);

        log.info("Correlation ID: {}. User cart retrieved successfully. User: {}", RequestData.getCorrelationId(), authentication.getName());

        return ResponseEntity.ok(cartResponseDTO);
    }
}
