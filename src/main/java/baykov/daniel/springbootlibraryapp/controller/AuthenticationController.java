package baykov.daniel.springbootlibraryapp.controller;

import baykov.daniel.springbootlibraryapp.payload.dto.ChangePasswordDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.ForgotPasswordDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.LoginDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.RegisterDTO;
import baykov.daniel.springbootlibraryapp.payload.response.JWTAuthenticationResponse;
import baykov.daniel.springbootlibraryapp.service.AuthenticationService;
import baykov.daniel.springbootlibraryapp.service.ConfirmationTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@Tag(name = "Authentication REST APIs")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ConfirmationTokenService confirmationTokenService;

    public AuthenticationController(AuthenticationService authenticationService, ConfirmationTokenService confirmationTokenService) {
        this.authenticationService = authenticationService;
        this.confirmationTokenService = confirmationTokenService;
    }

    @Operation(
            summary = "Login User REST API",
            description = "Login User REST API is used to login a particular user into database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PostMapping(value = {"login", "signin"})
    public ResponseEntity<JWTAuthenticationResponse> login(@Valid @RequestBody LoginDTO loginDTO) {
        String token = authenticationService.login(loginDTO);
        JWTAuthenticationResponse response = new JWTAuthenticationResponse();
        response.setAccessToken(token);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Register User REST API",
            description = "Register User REST API is used to register a new user into database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PostMapping(value = {"register", "signup"})
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDTO registerDTO) {
        String response = authenticationService.register(registerDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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
    @PatchMapping("/{userId}/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO,
                                                 @PathVariable Long userId) {
        return ResponseEntity.ok(authenticationService.changePassword(changePasswordDTO, userId));
    }

    @Operation(
            summary = "Confirm User's Email REST API",
            description = "Confirm User's Email REST API is used to confirm a new user's email by confirmation token"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @GetMapping("confirm")
    public ResponseEntity<String> confirmPassword(@RequestParam String token) {
        return ResponseEntity.ok(confirmationTokenService.confirmToken(token));
    }

    @Operation(
            summary = "Forgot Password Request REST API",
            description = "Forgot Password Request REST API is used to send reset token to user's email "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PostMapping("forgot")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        return ResponseEntity.ok(authenticationService.forgotPassword(forgotPasswordDTO));
    }

    @Operation(
            summary = "Reset User Password REST API",
            description = "Reset User Password REST API is used to reset user's password"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PutMapping("reset")
    public ResponseEntity<String> resetPassword(@RequestParam String token,
                                                @Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        return ResponseEntity.ok(authenticationService.resetPassword(forgotPasswordDTO, token));
    }
}
