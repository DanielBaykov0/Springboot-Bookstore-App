package baykov.daniel.springbootbookstoreapp.repository;

import baykov.daniel.springbootbookstoreapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(Role.RoleEnum name);
}
