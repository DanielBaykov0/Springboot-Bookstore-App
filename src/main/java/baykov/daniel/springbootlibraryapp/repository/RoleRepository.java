package baykov.daniel.springbootlibraryapp.repository;

import baykov.daniel.springbootlibraryapp.entity.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Tag(name = "Role Repository")
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Operation(
            summary = "Get Role By Name",
            description = "Search Role By Name is used to get a role from the database"
    )
    Optional<Role> findByName(String name);
}
