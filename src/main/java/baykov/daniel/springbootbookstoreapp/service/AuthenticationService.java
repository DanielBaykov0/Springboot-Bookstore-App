package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.TokenType;
import baykov.daniel.springbootbookstoreapp.entity.User;
import baykov.daniel.springbootbookstoreapp.entity.UserProfile;
import baykov.daniel.springbootbookstoreapp.exception.LibraryHTTPException;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.ChangePasswordDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.ForgotPasswordDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.LoginDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.RegisterDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.JwtRefreshRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.VerificationRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.JwtRefreshResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.JwtResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.mapper.UserMapper;
import baykov.daniel.springbootbookstoreapp.payload.mapper.UserProfileMapper;
import baykov.daniel.springbootbookstoreapp.repository.TokenRepository;
import baykov.daniel.springbootbookstoreapp.repository.UserProfileRepository;
import baykov.daniel.springbootbookstoreapp.repository.UserRepository;
import baykov.daniel.springbootbookstoreapp.security.util.JWTTokenProvider;
import baykov.daniel.springbootbookstoreapp.service.util.ServiceUtil;
import baykov.daniel.springbootbookstoreapp.utils.PropertyVariables;
import lombok.AllArgsConstructor;
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

import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.*;
import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.*;
import static baykov.daniel.springbootbookstoreapp.constant.Messages.*;

@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final ServiceUtil serviceUtil;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final TokenTypeService tokenTypeService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtTokenProvider;
    private final TokenService tokenService;
    private final EmailBuilderService emailBuilderService;
    private final EmailService emailService;
    private final MFAService mfaService;
    private final PropertyVariables propertyVariables;

    @Transactional
    public Map<String, String> register(RegisterDTO registerDTO) {
        log.info("Received request to register a new user with email: {}", registerDTO.getEmail());
        if (userRepository.existsByEmailIgnoreCase(registerDTO.getEmail())) {
            log.error("Registration failed. Email '{}' already exists.", registerDTO.getEmail());
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, EMAIL_ALREADY_EXISTS);
        }

        UserProfile userProfile = new UserProfile(UserProfileMapper.INSTANCE.registerDTOToEntity(registerDTO));
        userProfileRepository.save(userProfile);

        User user = new User(
                UserMapper.INSTANCE.registerDTOToEntity(registerDTO),
                passwordEncoder.encode(registerDTO.getPassword()),
                userProfile,
                serviceUtil.setRoles(),
                serviceUtil.getNewCart());

        userRepository.save(user);

        String token = tokenTypeService.createNewToken(user, TokenType.TokenTypeEnum.VERIFICATION);

        emailService.send("Email Confirmation", registerDTO.getEmail(),
                emailBuilderService.buildRegistrationEmail(
                        registerDTO.getFirstName(),
                        registerDTO.getLastName(),
                        registerDTO.getEmail(),
                        propertyVariables.getConfirmEmailUri() + token
                ));

        log.info("User registered successfully with email: {}", registerDTO.getEmail());
        return Map.of(MESSAGE, REGISTERED_SUCCESSFULLY);
    }

    public JwtResponseDTO login(LoginDTO loginDTO) {
        log.info("Received login request for user with email: {}", loginDTO.getEmail());
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDTO.getEmail(),
                                loginDTO.getPassword()));

        User user = userRepository.findByEmailIgnoreCase(loginDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(USER, EMAIL, loginDTO.getEmail()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        if (user.getMfaEnabled()) {
            log.info("User has MFA enabled. Generating new MFA secret for user: {}", loginDTO.getEmail());
            user.setSecret(mfaService.generateNewSecret());
            userRepository.save(user);
            log.info("User logged in successfully with MFA secret: {}", loginDTO.getEmail());
            return new JwtResponseDTO(
                    jwt,
                    refreshToken,
                    mfaService.generateQrCodeImageUri(user.getSecret()));
        }

        log.info("User logged in successfully without MFA secret: {}", loginDTO.getEmail());
        return new JwtResponseDTO(
                jwt,
                refreshToken,
                "");
    }

    public Map<String, String> logout() {
        SecurityContextHolder.clearContext();
        log.info("User logged out successfully.");
        return Map.of(MESSAGE, LOGOUT_SUCCESS);
    }

    public JwtRefreshResponseDTO refreshToken(JwtRefreshRequestDTO refreshRequestDTO) {
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
        log.info("Refreshed access token successfully.");
        return new JwtRefreshResponseDTO(accessToken, refreshToken);
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
        return Map.of(MESSAGE, PASSWORD_CHANGED_SUCCESSFULLY);
    }

    @Transactional
    public Map<String, String> forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
        User user = userRepository
                .findByEmailIgnoreCase(forgotPasswordDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(USER, EMAIL, forgotPasswordDTO.getEmail()));

        if (!user.getIsEmailVerified()) {
            log.error("Forgot Password failed for user with unverified email: {}", forgotPasswordDTO.getEmail());
            throw new LibraryHTTPException(HttpStatus.FORBIDDEN, EMAIL_NOT_VERIFIED);
        }

        tokenService.checkForPendingTokens(user, TokenType.TokenTypeEnum.RESET);

        String token = tokenTypeService.createNewToken(user, TokenType.TokenTypeEnum.RESET);
        serviceUtil.checkTokenValid(token);

        emailService.send("Email Reset Password", user.getEmail(),
                emailBuilderService.buildResetPasswordEmail(
                        user.getFirstName(),
                        user.getLastName(),
                        propertyVariables.getResetPasswordUri() + token));

        log.info("Forgot Password email sent to user with email: {}", user.getEmail());
        return Map.of(MESSAGE, EMAIL_RESET_SENT);
    }

    @Transactional
    public Map<String, String> resendForgotPassword(String token) {
        Long userId = tokenRepository.findUserIdByToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER, ID, userId));

        tokenService.checkForPendingTokens(user, TokenType.TokenTypeEnum.RESET);

        String newToken = tokenTypeService.createNewToken(user, TokenType.TokenTypeEnum.RESET);
        serviceUtil.checkTokenValid(newToken);

        emailService.send("Email Reset Password", user.getEmail(),
                emailBuilderService.buildResetPasswordEmail(
                        user.getFirstName(),
                        user.getLastName(),
                        propertyVariables.getResetPasswordUri() + newToken));

        log.info("Resent Forgot Password email to user with email: {}", user.getEmail());
        return Map.of(MESSAGE, SUCCESSFULLY_RESEND_FORGOT_PASSWORD);
    }

    @Transactional
    public Map<String, String> sendEmailVerification(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException(USER, EMAIL, email));

        if (user.getIsEmailVerified()) {
            log.error("Email Verification failed for user with already verified email: {}", email);
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, EMAIL_ALREADY_VERIFIED);
        }

        tokenService.checkForPendingTokens(user, TokenType.TokenTypeEnum.VERIFICATION);

        String newToken = tokenTypeService.createNewToken(user, TokenType.TokenTypeEnum.VERIFICATION);
        serviceUtil.checkTokenValid(newToken);

        emailService.send("Email Confirmation", user.getEmail(),
                emailBuilderService.buildConfirmationEmail(
                        user.getFirstName(),
                        user.getLastName(),
                        propertyVariables.getConfirmEmailUri() + newToken));


        log.info("Sent Email Verification email to user with email: {}", user.getEmail());
        return Map.of(MESSAGE, SUCCESSFULLY_RESEND_VERIFICATION_EMAIL);
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
        return Map.of(MESSAGE, EMAIL_CONFIRMED_SUCCESSFULLY);
    }

    public boolean verifyCode(VerificationRequestDTO verificationRequestDTO, Authentication authentication) {
        User user = userRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(USER, EMAIL, authentication.getName()));

        if (mfaService.isOtpNotValid(user.getSecret(), verificationRequestDTO.getCode())) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, VERIFICATION_CODE_NOT_CORRECT);
        }

        log.info("Verification code verified successfully for user with email: {}", authentication.getName());
        return true;
    }
}
