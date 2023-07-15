package baykov.daniel.springbootlibraryapp.service.impl;

import baykov.daniel.springbootlibraryapp.entity.Role;
import baykov.daniel.springbootlibraryapp.entity.User;
import baykov.daniel.springbootlibraryapp.exception.LibraryHTTPException;
import baykov.daniel.springbootlibraryapp.payload.dto.LoginDTO;
import baykov.daniel.springbootlibraryapp.payload.dto.RegisterDTO;
import baykov.daniel.springbootlibraryapp.repository.RoleRepository;
import baykov.daniel.springbootlibraryapp.repository.UserRepository;
import baykov.daniel.springbootlibraryapp.security.JWTTokenProvider;
import baykov.daniel.springbootlibraryapp.service.AuthenticationService;
import baykov.daniel.springbootlibraryapp.utils.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtTokenProvider;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public String login(LoginDTO loginDTO) {
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
        return Messages.USER_REGISTERED_SUCCESSFULLY_MESSAGE;
    }
}
