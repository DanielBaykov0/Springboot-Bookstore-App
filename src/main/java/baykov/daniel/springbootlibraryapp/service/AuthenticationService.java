package baykov.daniel.springbootlibraryapp.service;

import baykov.daniel.springbootlibraryapp.constant.Messages;
import baykov.daniel.springbootlibraryapp.entity.TokenType;
import baykov.daniel.springbootlibraryapp.entity.User;
import baykov.daniel.springbootlibraryapp.exception.LibraryHTTPException;
import baykov.daniel.springbootlibraryapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootlibraryapp.payload.dto.*;
import baykov.daniel.springbootlibraryapp.payload.mapper.UserMapper;
import baykov.daniel.springbootlibraryapp.payload.response.JwtResponse;
import baykov.daniel.springbootlibraryapp.repository.TokenRepository;
import baykov.daniel.springbootlibraryapp.repository.UserRepository;
import baykov.daniel.springbootlibraryapp.security.util.JWTTokenProvider;
import baykov.daniel.springbootlibraryapp.service.helper.AuthenticationServiceHelper;
import baykov.daniel.springbootlibraryapp.service.helper.TokenServiceHelper;
import baykov.daniel.springbootlibraryapp.utils.PropertyVariables;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static baykov.daniel.springbootlibraryapp.constant.AppConstants.*;
import static baykov.daniel.springbootlibraryapp.constant.ErrorMessages.*;
import static baykov.daniel.springbootlibraryapp.constant.Messages.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationServiceHelper authenticationServiceHelper;
    private final UserRepository userRepository;
    private final TokenTypeService tokenTypeService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtTokenProvider;
    private final TokenService tokenService;
    private final TokenServiceHelper tokenServiceHelper;
    private final EmailBuilderService emailBuilderService;
    private final EmailService emailService;
    private final PropertyVariables propertyVariables;

    @Transactional
    public Map<String, String> register(RegisterDTO registerDTO) {
        if (userRepository.existsByEmailIgnoreCase(registerDTO.getEmail())) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.EMAIL_ALREADY_EXISTS);
        }

        User user = new User(
                UserMapper.INSTANCE.registerDTOToEntity(registerDTO),
                passwordEncoder.encode(registerDTO.getPassword()),
                authenticationServiceHelper.setRoles());

        userRepository.save(user);

        String token = tokenTypeService.createNewToken(user, TokenType.TokenTypeEnum.VERIFICATION);

        emailService.send("Email Confirmation", registerDTO.getEmail(),
                emailBuilderService.buildRegistrationEmail(
                        registerDTO.getFirstName(),
                        registerDTO.getLastName(),
                        registerDTO.getEmail(),
                        propertyVariables.getConfirmEmailUri() + token
                ));

        return Map.of(MESSAGE, Messages.REGISTERED_SUCCESSFULLY);
    }

    public JwtResponse login(LoginDTO loginDTO) {
        User user = userRepository.findByEmailIgnoreCase(loginDTO.getEmail())
                .orElseThrow(() -> new LibraryHTTPException(HttpStatus.NOT_FOUND, Messages.USER_NOT_EXISTS));

        if (!user.getIsEmailVerified()) {
            throw new LibraryHTTPException(HttpStatus.UNAUTHORIZED, Messages.EMAIL_NOT_CONFIRMED);
        }

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        return new JwtResponse(jwt, refreshToken);
    }

    public Map<String, String> logout() {
        SecurityContextHolder.clearContext();
        return Map.of(MESSAGE, LOGOUT_SUCCESS);
    }

    public JwtResponse refreshToken(JwtRefreshRequestDTO refreshRequestDTO) {
        String requestRefreshToken = refreshRequestDTO.getRefreshToken();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new LibraryHTTPException(HttpStatus.UNAUTHORIZED, NOT_AUTHENTICATED);
        }

        if (!jwtTokenProvider.validateToken(requestRefreshToken)) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, REFRESH_TOKEN_EXPIRED);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        return new JwtResponse(accessToken, refreshToken);
    }

    @Transactional
    public Map<String, String> changePassword(ChangePasswordDTO changePasswordDTO, String token) {
        Long userId = tokenRepository.findUserIdByToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER, ID, userId));

        tokenService.validateToken(token, TokenType.TokenTypeEnum.RESET);

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword()));
        userRepository.save(user);

        log.info("Password changed successfully for user with id: {}", userId);
        return Map.of("message", PASSWORD_CHANGED_SUCCESSFULLY);
    }

    @Transactional
    public Map<String, String> forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
        User user = userRepository
                .findByEmailIgnoreCase(forgotPasswordDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(USER, EMAIL, forgotPasswordDTO.getEmail()));

        if (!user.getIsEmailVerified()) {
            log.error("Forgot Password failed for user with unverified email: {}", forgotPasswordDTO.getEmail());
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, EMAIL_NOT_VERIFIED);
        }

        tokenService.checkForPendingTokens(user, TokenType.TokenTypeEnum.RESET);

        String token = tokenTypeService.createNewToken(user, TokenType.TokenTypeEnum.RESET);
        tokenServiceHelper.checkTokenValid(token);

        emailService.send("Email Reset Password", user.getEmail(),
                emailBuilderService.buildResetPasswordEmail(
                        user.getFirstName(),
                        user.getLastName(),
                        propertyVariables.getResetPasswordUri() + token));

        log.info("Forgot Password email sent to user with email: {}", user.getEmail());
        return Map.of("message", EMAIL_RESET_SENT);
    }

    @Transactional
    public Map<String, String> verifyEmail(String token) {
        log.info("Verifying email with token: {}", token);
        tokenService.validateToken(token, TokenType.TokenTypeEnum.VERIFICATION);

        Long userId = tokenRepository.findUserIdByToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER, ID, userId));

        user.setIsEmailVerified(true);
        userRepository.save(user);

        log.info("Email verified successfully for user with email: {}", user.getEmail());
        return Map.of(MESSAGE, Messages.EMAIL_CONFIRMED_SUCCESSFULLY);
    }
}
