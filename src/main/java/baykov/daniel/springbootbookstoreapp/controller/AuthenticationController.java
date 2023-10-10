package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.config.RequestData;
import baykov.daniel.springbootbookstoreapp.payload.dto.ChangePasswordDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.ForgotPasswordDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.LoginDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.RegisterDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.JwtRefreshRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.VerificationRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.JwtRefreshResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.JwtResponseDTO;
import baykov.daniel.springbootbookstoreapp.service.AuthenticationService;
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

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
@Tag(name = "REST APIs Authentication Resource")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Register User REST API",
            description = "Register User REST API is used to register a new user into database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PostMapping(value = "/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterDTO registerDTO) {
        log.info("Correlation ID: {}. Received request to register a new user.", RequestData.getCorrelationId());

        Map<String, String> response = authenticationService.register(registerDTO);

        log.info("Correlation ID: {}. User registration completed successfully.", RequestData.getCorrelationId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Login User REST API",
            description = "Login User REST API is used to login a particular user into database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PostMapping(value = "/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        log.info("Correlation ID: {}. Received login request for user: {}.", RequestData.getCorrelationId(), loginDTO.getEmail());

        JwtResponseDTO jwtResponseDTO = authenticationService.login(loginDTO);

        log.info("Correlation ID: {}. User login successful for user: {}.", RequestData.getCorrelationId(), loginDTO.getEmail());
        return ResponseEntity.ok(jwtResponseDTO);
    }

    @Operation(
            summary = "Verify Code from QR Code Image API",
            description = "Verify Code from QR Code Image API is used to verify the 6 digit code generated from the qr image"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(
            @Valid @RequestBody VerificationRequestDTO verificationRequestDTO, Authentication authentication) {
        log.info("Correlation ID: {}. Received verification request for user: {}.", RequestData.getCorrelationId(), authentication.getName());

        boolean verificationSuccessful = authenticationService.verifyCode(verificationRequestDTO, authentication);
        log.info("Correlation ID: {}. Verification successful for user: {}.", RequestData.getCorrelationId(), authentication.getName());
        return ResponseEntity.ok(verificationSuccessful);
    }

    @Operation(
            summary = "Get Current Logged in User API",
            description = "Get Current Logged in User API is used to get the email of the current logged in user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    @GetMapping("/me")
    public ResponseEntity<String> currentUser(Authentication authentication) {
        log.info("Correlation ID: {}. Retrieving current user for authentication: {}.", RequestData.getCorrelationId(), authentication);

        String currentUserName = authentication.getName();
        log.info("Correlation ID: {}. Current user retrieved: {}.", RequestData.getCorrelationId(), currentUserName);

        return ResponseEntity.ok(currentUserName);
    }

    @Operation(
            summary = "Logout REST API",
            description = "Logout API is used to successfully logout out of the application"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        log.info("Correlation ID: {}. Logging out user.", RequestData.getCorrelationId());

        Map<String, String> response = authenticationService.logout();

        log.info("Correlation ID: {}. User logged out successfully.", RequestData.getCorrelationId());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get New Refresh Token REST API",
            description = "Get New Refresh Token API is used to request a new refresh token when the old one has expired"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtRefreshResponseDTO> refreshToken(@Valid @RequestBody JwtRefreshRequestDTO refreshRequestDTO) {
        log.info("Correlation ID: {}. Refreshing token for user...", RequestData.getCorrelationId());

        JwtRefreshResponseDTO responseDTO = authenticationService.refreshToken(refreshRequestDTO);

        log.info("Correlation ID: {}. Token refreshed for user.", RequestData.getCorrelationId());
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(
            summary = "Change User Password REST API",
            description = "Change User Password API is used to change the password of an existing user in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    @PatchMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO,
            @RequestParam String token) {
        log.info("Change password request received. Correlation ID: {}", RequestData.getCorrelationId());

        Map<String, String> response = authenticationService.changePassword(changePasswordDTO, token);

        log.info("Change password request completed. Correlation ID: {}", RequestData.getCorrelationId());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Forgot Password Request REST API",
            description = "Forgot Password Request REST API is used to send reset token to user's email "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PostMapping("/forgot")
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        log.info("Forgot password request received. Correlation ID: {}", RequestData.getCorrelationId());

        Map<String, String> response = authenticationService.forgotPassword(forgotPasswordDTO);

        log.info("Forgot password request completed. Correlation ID: {}", RequestData.getCorrelationId());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Forgot Password Resend Request REST API",
            description = "Forgot Password Resend Request REST API is used to resend reset token to user's email "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PostMapping("/resend-forgot")
    public ResponseEntity<Map<String, String>> resendForgotPassword(@RequestParam String token) {
        log.info("Resend forgot password request received. Correlation ID: {}", RequestData.getCorrelationId());

        Map<String, String> response = authenticationService.resendForgotPassword(token);

        log.info("Resend forgot password request completed. Correlation ID: {}", RequestData.getCorrelationId());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Send Email Verification REST API",
            description = "Send Email Verification REST API is used to send to the user's email a confirmation link"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    @PostMapping("/send-email-verification")
    public ResponseEntity<Map<String, String>> sendEmailVerification(Authentication authentication) {
        log.info("Sending email verification request received. Correlation ID: {}", RequestData.getCorrelationId());

        Map<String, String> response = authenticationService.sendEmailVerification(authentication.getName());

        log.info("Sending email verification request completed. Correlation ID: {}", RequestData.getCorrelationId());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Confirm User's Email REST API",
            description = "Confirm User's Email REST API is used to confirm a new user's email by confirmation token"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN', 'USER')")
    @GetMapping("/verify-email")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam String token) {
        log.info("Verification email request received. Correlation ID: {}", RequestData.getCorrelationId());

        Map<String, String> response = authenticationService.verifyEmail(token);

        log.info("Verification email completed. Correlation ID: {}", RequestData.getCorrelationId());
        return ResponseEntity.ok(response);
    }
}
