package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.config.RequestData;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.OrderHistoryResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.UserProfileEmailRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.UserProfileRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.ProductProfileResponseDTO;
import baykov.daniel.springbootbookstoreapp.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/profile")
@Tag(name = "REST APIs for User Profile Resource")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Operation(
            summary = "Update User Profile API",
            description = "Update User API is used to update user information in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    @PutMapping
    public ResponseEntity<Map<String, String>> updateUserProfile(
            @RequestBody @Valid UserProfileRequestDTO userProfileRequestDTO, Authentication authentication) {
        log.info("Correlation ID: {}. Received request to update user profile.", RequestData.getCorrelationId());

        Map<String, String> response = userProfileService.updateUserProfile(userProfileRequestDTO, authentication);

        log.info("Correlation ID: {}. User profile updated successfully.", RequestData.getCorrelationId());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update User Profile Email API",
            description = "Update User Email API is used to update user email in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    @PutMapping("/email")
    public ResponseEntity<Map<String, String>> updateUserProfileEmail(
            @RequestBody @Valid UserProfileEmailRequestDTO userProfileEmailRequestDTO, Authentication authentication) {
        log.info("Correlation ID: {}. Received request to update user profile email.", RequestData.getCorrelationId());

        Map<String, String> response = userProfileService.updateUserProfileEmail(userProfileEmailRequestDTO, authentication);

        log.info("Correlation ID: {}. User profile email updated successfully.", RequestData.getCorrelationId());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update User MFA Status API",
            description = "Update User MFA Status API is used to update user mfa status in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    @PutMapping("/mfa")
    public ResponseEntity<Map<String, String>> updateUserMfaStatus(Authentication authentication) {
        log.info("Correlation ID: {}. Received request to update user MFA status.", RequestData.getCorrelationId());

        Map<String, String> response = userProfileService.updateUserMfa(authentication);

        log.info("Correlation ID: {}. User MFA status updated successfully.", RequestData.getCorrelationId());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get User Products API",
            description = "Get User Products API is used to fetch user's products"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    @GetMapping("/my-products")
    public ResponseEntity<List<ProductProfileResponseDTO>> listUserProducts(Authentication authentication) {
        log.info("Correlation ID: {}. Received request to list user products.", RequestData.getCorrelationId());

        List<ProductProfileResponseDTO> response = userProfileService.getProducts(authentication);

        log.info("Correlation ID: {}. User products listed successfully.", RequestData.getCorrelationId());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get User Orders History API",
            description = "Get User Orders History API is used to fetch user's orders history"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderHistoryResponseDTO>> listUserOrdersHistory(Authentication authentication) {
        log.info("Correlation ID: {}. Received request to list user orders history.", RequestData.getCorrelationId());

        List<OrderHistoryResponseDTO> response = userProfileService.getOrdersHistory(authentication);

        log.info("Correlation ID: {}. User orders history listed successfully.", RequestData.getCorrelationId());
        return ResponseEntity.ok(response);
    }
}
