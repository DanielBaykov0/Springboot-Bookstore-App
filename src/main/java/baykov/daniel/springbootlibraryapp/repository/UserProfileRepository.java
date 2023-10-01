package baykov.daniel.springbootlibraryapp.repository;

import baykov.daniel.springbootlibraryapp.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByEmailIgnoreCase(String email);
}
