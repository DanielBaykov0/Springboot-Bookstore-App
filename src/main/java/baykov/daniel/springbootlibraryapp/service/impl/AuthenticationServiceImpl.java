package baykov.daniel.springbootlibraryapp.service.impl;

import baykov.daniel.springbootlibraryapp.entity.ConfirmationToken;
import baykov.daniel.springbootlibraryapp.entity.Role;
import baykov.daniel.springbootlibraryapp.entity.User;
import baykov.daniel.springbootlibraryapp.exception.LibraryHTTPException;
import baykov.daniel.springbootlibraryapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootlibraryapp.payload.dto.ChangePasswordDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.ForgotPasswordDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.LoginDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.RegisterDTO;
import baykov.daniel.springbootlibraryapp.repository.RoleRepository;
import baykov.daniel.springbootlibraryapp.repository.UserRepository;
import baykov.daniel.springbootlibraryapp.security.JWTTokenProvider;
import baykov.daniel.springbootlibraryapp.service.AuthenticationService;
import baykov.daniel.springbootlibraryapp.service.ConfirmationTokenService;
import baykov.daniel.springbootlibraryapp.service.EmailService;
import baykov.daniel.springbootlibraryapp.utils.AppConstants;
import baykov.daniel.springbootlibraryapp.utils.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtTokenProvider;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                     UserRepository userRepository,
                                     RoleRepository roleRepository,
                                     PasswordEncoder passwordEncoder,
                                     JWTTokenProvider jwtTokenProvider,
                                     ConfirmationTokenService confirmationTokenService,
                                     EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.confirmationTokenService = confirmationTokenService;
        this.emailService = emailService;
    }


    @Override
    public String login(LoginDTO loginDTO) {
        User user = userRepository
                .getByUsernameOrEmail(loginDTO.getUsernameOrEmail(), loginDTO.getUsernameOrEmail())
                .orElseThrow(() -> new LibraryHTTPException(HttpStatus.NOT_FOUND, Messages.USER_NOT_EXIST_MESSAGE));
        if (!user.isConfirmed()) {
            throw new LibraryHTTPException(HttpStatus.UNAUTHORIZED, Messages.EMAIL_NOT_CONFIRMED_MESSAGE);
        }
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public String register(RegisterDTO registerDTO) {
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.USERNAME_ALREADY_EXISTS_MESSAGE);
        }

        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.EMAIL_ALREADY_EXISTS_MESSAGE);
        }

        User user = new User();
        user.setName(registerDTO.getName());
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setCity(registerDTO.getCity());
        int userAge = LocalDate.now().getYear() - registerDTO.getDateOfBirth().getYear();
        user.setAge(userAge);
        user.setAddress(registerDTO.getAddress());
        user.setCounty(registerDTO.getCountry());
        user.setGender(registerDTO.getGender());
        user.setEmail(registerDTO.getEmail());
        user.setDateOfBirth(registerDTO.getDateOfBirth());

        Set<Role> roles = new HashSet<>();
        Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
        Role role = new Role();
        if (userRole.isPresent()) {
            role = userRole.get();
        }
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        confirmationToken.setUser(user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String confirmationLink = AppConstants.CONFIRMATION_LINK + token;
        emailService.send(registerDTO.getEmail(), emailService.buildEmail(registerDTO.getName(), confirmationLink));
        return Messages.USER_REGISTERED_SUCCESSFULLY_MESSAGE + token;
    }

    @Override
    public String changePassword(ChangePasswordDTO changePasswordDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), changePasswordDTO.getOldPassword()));

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword())) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.NEW_PASSWORD_NO_MATCH_MESSAGE);
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
        emailService.send(user.getEmail(), emailService.buildEmailChangePassword(user.getName()));
        return Messages.PASSWORD_CHANGED_SUCCESSFULLY_MESSAGE;
    }

    @Override
    public String forgotPassword(ForgotPasswordDTO forgotPasswordDTO) {
        User user = userRepository
                .getByUsernameOrEmail(forgotPasswordDTO.getUsernameOrEmail(), forgotPasswordDTO.getUsernameOrEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username or email", forgotPasswordDTO.getUsernameOrEmail()));

        String userEmail = user.getEmail();
        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        confirmationToken.setUser(user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String confirmationLink = AppConstants.RESET_LINK + token;
        emailService.send(userEmail, emailService.buildEmailForgotPassword(user.getName(), confirmationLink));
        return Messages.EMAIL_RESET_SENT_MESSAGE;
    }

    @Override
    public String resetPassword(ForgotPasswordDTO forgotPasswordDTO, String token) {
        User user = confirmationTokenService.confirmResetToken(token).getUser();
        if (!forgotPasswordDTO.getNewPassword().equals(forgotPasswordDTO.getConfirmNewPassword())) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.NEW_PASSWORD_NO_MATCH_MESSAGE);
        }

        user.setPassword(passwordEncoder.encode(forgotPasswordDTO.getConfirmNewPassword()));
        userRepository.save(user);
        return Messages.PASSWORD_CHANGED_SUCCESSFULLY_MESSAGE;
    }
}
