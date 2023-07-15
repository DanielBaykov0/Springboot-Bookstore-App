package baykov.daniel.springbootlibraryapp.controller;

import baykov.daniel.springbootlibraryapp.payload.dto.ChangePasswordDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.ForgotPasswordDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.LoginDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.RegisterDTO;
import baykov.daniel.springbootlibraryapp.payload.response.JWTAuthenticationResponse;
import baykov.daniel.springbootlibraryapp.service.AuthenticationService;
import baykov.daniel.springbootlibraryapp.service.ConfirmationTokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ConfirmationTokenService confirmationTokenService;

    public AuthenticationController(AuthenticationService authenticationService, ConfirmationTokenService confirmationTokenService) {
        this.authenticationService = authenticationService;
        this.confirmationTokenService = confirmationTokenService;
    }

    @PostMapping(value = {"login", "signin"})
    public ResponseEntity<JWTAuthenticationResponse> login(@Valid @RequestBody LoginDTO loginDTO) {
        String token = authenticationService.login(loginDTO);
        JWTAuthenticationResponse response = new JWTAuthenticationResponse();
        response.setAccessToken(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = {"register", "signup"})
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDTO registerDTO) {
        String response = authenticationService.register(registerDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO,
                                                 @PathVariable Long userId) {
        return ResponseEntity.ok(authenticationService.changePassword(changePasswordDTO, userId));
    }

    @GetMapping("confirm")
    public ResponseEntity<String> confirmPassword(@RequestParam String token) {
        return ResponseEntity.ok(confirmationTokenService.confirmToken(token));
    }

    @PostMapping("forgot")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        return ResponseEntity.ok(authenticationService.forgotPassword(forgotPasswordDTO));
    }

    @PutMapping("reset")
    public ResponseEntity<String> resetPassword(@RequestParam String token,
                                                @Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        return ResponseEntity.ok(authenticationService.resetPassword(forgotPasswordDTO, token));
    }
}
