package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.TokenType;
import baykov.daniel.springbootbookstoreapp.entity.User;
import baykov.daniel.springbootbookstoreapp.entity.UserProfile;
import baykov.daniel.springbootbookstoreapp.exception.LibraryHTTPException;
import baykov.daniel.springbootbookstoreapp.payload.dto.ChangePasswordDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.ForgotPasswordDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.LoginDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.RegisterDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.JwtRefreshRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.VerificationRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.JwtRefreshResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.JwtResponseDTO;
import baykov.daniel.springbootbookstoreapp.repository.TokenRepository;
import baykov.daniel.springbootbookstoreapp.repository.UserProfileRepository;
import baykov.daniel.springbootbookstoreapp.repository.UserRepository;
import baykov.daniel.springbootbookstoreapp.security.util.JWTTokenProvider;
import baykov.daniel.springbootbookstoreapp.service.util.ServiceUtil;
import baykov.daniel.springbootbookstoreapp.utils.PropertyVariables;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static baykov.daniel.springbootbookstoreapp.constant.Messages.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ServiceUtil serviceUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private TokenTypeService tokenTypeService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTTokenProvider jwtTokenProvider;

    @Mock
    private TokenService tokenService;

    @Mock
    private EmailBuilderService emailBuilderService;

    @Mock
    private EmailService emailService;

    @Mock
    private MFAService mfaService;

    @Mock
    private PropertyVariables propertyVariables;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(
                authenticationManager,
                serviceUtil,
                userRepository,
                userProfileRepository,
                tokenTypeService,
                tokenRepository,
                passwordEncoder,
                jwtTokenProvider,
                tokenService,
                emailBuilderService,
                emailService,
                mfaService,
                propertyVariables
        );
    }

    @Test
    void testRegister_Success() {
        RegisterDTO registerDTO = new RegisterDTO();

        when(userRepository.existsByEmailIgnoreCase(registerDTO.getEmail())).thenReturn(false);

        UserProfile userProfile = new UserProfile();
        when(userProfileRepository.save(any())).thenReturn(userProfile);

        when(passwordEncoder.encode(registerDTO.getPassword())).thenReturn("hashedPassword");

        String token = "sampleToken";
        when(tokenTypeService.createNewToken(any(), any())).thenReturn(token);

        String emailBody = "Sample email body";
        when(emailBuilderService.buildRegistrationEmail(
                eq(registerDTO.getFirstName()),
                eq(registerDTO.getLastName()),
                eq(registerDTO.getEmail()),
                anyString())).thenReturn(emailBody);

        Map<String, String> result = authenticationService.register(registerDTO);

        verify(userRepository, times(1)).existsByEmailIgnoreCase(registerDTO.getEmail());
        verify(userProfileRepository, times(1)).save(any(UserProfile.class));
        verify(userRepository, times(1)).save(any());
        verify(emailService, times(1)).send(
                eq("Email Confirmation"),
                eq(registerDTO.getEmail()),
                eq(emailBody));
        verify(tokenTypeService, times(1)).createNewToken(any(), any(TokenType.TokenTypeEnum.class));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(REGISTERED_SUCCESSFULLY, result.get("message"));
    }

    @Test
    void testLogin_Success() {
        LoginDTO loginDTO = new LoginDTO();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(),
                loginDTO.getPassword());
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        User user = new User();
        user.setMfaEnabled(false);
        when(userRepository.findByEmailIgnoreCase(loginDTO.getEmail())).thenReturn(Optional.of(user));

        String jwt = "sampleJWT";
        String refreshToken = "sampleRefreshToken";
        when(jwtTokenProvider.generateAccessToken(any())).thenReturn(jwt);
        when(jwtTokenProvider.generateRefreshToken(any())).thenReturn(refreshToken);

        JwtResponseDTO result = authenticationService.login(loginDTO);

        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, times(1)).findByEmailIgnoreCase(loginDTO.getEmail());
        verify(jwtTokenProvider, times(1)).generateAccessToken(any());
        verify(jwtTokenProvider, times(1)).generateRefreshToken(any());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(jwt, result.getAccessToken());
        Assertions.assertEquals(refreshToken, result.getRefreshToken());
        Assertions.assertEquals("", result.getSecretImageUri());
    }

    @Test
    void testLogout_Success() {
        SecurityContextHolder.setContext(new SecurityContextImpl());

        Map<String, String> result = authenticationService.logout();

        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(LOGOUT_SUCCESS, result.get("message"));
    }

    @Test
    void testRefreshToken_Success() {
        JwtRefreshRequestDTO refreshRequestDTO = new JwtRefreshRequestDTO();
        refreshRequestDTO.setRefreshToken("valid_refresh_token");

        Authentication authentication = new UsernamePasswordAuthenticationToken("test@example.com", "password");
        SecurityContextImpl context = new SecurityContextImpl();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        when(jwtTokenProvider.validateToken("valid_refresh_token")).thenReturn(true);
        when(jwtTokenProvider.generateAccessToken(authentication)).thenReturn("new_access_token");
        when(jwtTokenProvider.generateRefreshToken(authentication)).thenReturn("new_refresh_token");

        JwtRefreshResponseDTO result = authenticationService.refreshToken(refreshRequestDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("new_access_token", result.getAccessToken());
        Assertions.assertEquals("new_refresh_token", result.getRefreshToken());
    }

    @Test
    void testChangePassword_Success() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setPassword("new_password");

        String token = "reset_token";
        Long userId = 123L;

        when(tokenRepository.findUserIdByToken(token)).thenReturn(userId);

        User user = new User();
        user.setId(userId);
        user.setPassword("old_password");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(changePasswordDTO.getPassword())).thenReturn(changePasswordDTO.getPassword());
        when(userRepository.save(user)).thenReturn(user);

        Map<String, String> result = authenticationService.changePassword(changePasswordDTO, token);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(PASSWORD_CHANGED_SUCCESSFULLY, result.get("message"));
        Assertions.assertEquals("new_password", user.getPassword());
    }

    @Test
    void testForgotPassword_Success() {
        ForgotPasswordDTO forgotPasswordDTO = new ForgotPasswordDTO();
        forgotPasswordDTO.setEmail("test@example.com");

        User user = new User();
        user.setEmail(forgotPasswordDTO.getEmail());
        user.setIsEmailVerified(true);
        when(userRepository.findByEmailIgnoreCase(forgotPasswordDTO.getEmail())).thenReturn(Optional.of(user));

        String token = "reset_token";
        String resetPasswordUri = "resetPasswordUrl";
        when(tokenTypeService.createNewToken(user, TokenType.TokenTypeEnum.RESET)).thenReturn(token);
        when(propertyVariables.getResetPasswordUri()).thenReturn(resetPasswordUri);
        when(emailBuilderService.buildResetPasswordEmail(user.getFirstName(), user.getLastName(),
                resetPasswordUri + token)).thenReturn("Mock email content");

        Map<String, String> result = authenticationService.forgotPassword(forgotPasswordDTO);

        Assertions.assertNotNull(result);
        verify(emailService, times(1)).send("Email Reset Password",
                user.getEmail(),
                "Mock email content");
        Assertions.assertEquals(EMAIL_RESET_SENT, result.get("message"));
    }

    @Test
    void testResendForgotPassword_Success() {
        String token = "reset_token";
        Long userId = 1L;

        when(tokenRepository.findUserIdByToken(token)).thenReturn(userId);

        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        String newToken = "new_reset_token";
        String resetPasswordUri = "resetPasswordUrl";
        when(tokenTypeService.createNewToken(user, TokenType.TokenTypeEnum.RESET)).thenReturn(newToken);
        when(propertyVariables.getResetPasswordUri()).thenReturn(resetPasswordUri);
        when(emailBuilderService.buildResetPasswordEmail(user.getFirstName(), user.getLastName(),
                resetPasswordUri + newToken)).thenReturn("Mock email content");

        Map<String, String> result = authenticationService.resendForgotPassword(token);

        Assertions.assertNotNull(result);
        verify(emailService, times(1)).send("Email Reset Password",
                user.getEmail(),
                "Mock email content");
        Assertions.assertEquals(SUCCESSFULLY_RESEND_FORGOT_PASSWORD, result.get("message"));
    }

    @Test
    void testSendEmailVerification_Success() {
        String email = "test@example.com";

        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setIsEmailVerified(false);
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(java.util.Optional.of(user));

        String newToken = "verification_token";
        String confirmEmailUri = "confirmEmailUri";
        when(tokenTypeService.createNewToken(user, TokenType.TokenTypeEnum.VERIFICATION)).thenReturn(newToken);
        when(propertyVariables.getConfirmEmailUri()).thenReturn(confirmEmailUri);
        when(emailBuilderService.buildConfirmationEmail(user.getFirstName(), user.getLastName(),
                confirmEmailUri + newToken)).thenReturn("Mock email content");

        Map<String, String> result = authenticationService.sendEmailVerification(email);

        Assertions.assertNotNull(result);
        verify(emailService, times(1)).send("Email Confirmation",
                user.getEmail(),
                "Mock email content");
        Assertions.assertEquals(SUCCESSFULLY_RESEND_VERIFICATION_EMAIL, result.get("message"));
    }

    @Test
    void testVerifyEmail_Success() {
        String token = "verification_token";
        Long userId = 1L;

        when(tokenRepository.findUserIdByToken(token)).thenReturn(userId);
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setIsEmailVerified(false);
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        Map<String, String> result = authenticationService.verifyEmail(token);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(user.getIsEmailVerified());
        Assertions.assertEquals(EMAIL_CONFIRMED_SUCCESSFULLY, result.get("message"));
    }

    @Test
    void testVerifyCode_Success() {
        String email = "test@example.com";
        String verificationCode = "123456";
        VerificationRequestDTO verificationRequestDTO = new VerificationRequestDTO();
        verificationRequestDTO.setCode(verificationCode);

        User user = new User();
        user.setEmail(email);
        user.setSecret("user_secret");
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(java.util.Optional.of(user));
        when(mfaService.isOtpNotValid(user.getSecret(), verificationCode)).thenReturn(false);

        boolean result = authenticationService.verifyCode(verificationRequestDTO, new UsernamePasswordAuthenticationToken(email, null));

        Assertions.assertTrue(result);
    }

    @Test
    void testVerifyCode_CodeInvalid_ReturnFalse() {
        String email = "test@example.com";
        String verificationCode = "123456";
        VerificationRequestDTO verificationRequestDTO = new VerificationRequestDTO();
        verificationRequestDTO.setCode(verificationCode);

        User user = new User();
        user.setEmail(email);
        user.setSecret("user_secret");
        when(userRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(user));
        when(mfaService.isOtpNotValid(user.getSecret(), verificationCode)).thenReturn(true);

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> authenticationService.verifyCode(verificationRequestDTO, new UsernamePasswordAuthenticationToken(email, null))
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(VERIFICATION_CODE_NOT_CORRECT, exception.getMessage());
    }
}