package baykov.daniel.springbootlibraryapp.service.helper;

import baykov.daniel.springbootlibraryapp.entity.Role;
import baykov.daniel.springbootlibraryapp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthenticationServiceHelper {

    private final RoleRepository roleRepository;

    public Set<Role> setRoles() {
        Set<Role> roles = new HashSet<>();
        Optional<Role> userRole = roleRepository.findByName(Role.RoleEnum.USER);
        Role role = new Role();
        if (userRole.isPresent()) {
            role = userRole.get();
        }
        roles.add(role);
        return roles;
    }
}
