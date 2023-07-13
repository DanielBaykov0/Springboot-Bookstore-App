package baykov.daniel.springbootlibraryapp.controllers;

import baykov.daniel.springbootlibraryapp.payload.dto.LoginDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.RegisterDTO;
import baykov.daniel.springbootlibraryapp.payload.response.JWTAuthenticationResponse;
import baykov.daniel.springbootlibraryapp.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
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
}
