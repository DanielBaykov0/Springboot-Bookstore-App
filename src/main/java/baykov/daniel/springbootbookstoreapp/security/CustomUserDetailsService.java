package baykov.daniel.springbootbookstoreapp.security;

import baykov.daniel.springbootbookstoreapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.USER_NOT_FOUND_BY_EMAIL;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        baykov.daniel.springbootbookstoreapp.entity.User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_BY_EMAIL + email));

        Set<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority((role.getName().toString())))
                .collect(Collectors.toSet());
        return new User(user.getEmail(), user.getPassword(), authorities);
    }
}
