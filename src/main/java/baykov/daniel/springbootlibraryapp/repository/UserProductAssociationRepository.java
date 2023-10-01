package baykov.daniel.springbootlibraryapp.repository;

import baykov.daniel.springbootlibraryapp.entity.UserProductAssociation;
import baykov.daniel.springbootlibraryapp.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProductAssociationRepository extends JpaRepository<UserProductAssociation, Long> {

    List<UserProductAssociation> findByUserProfile(UserProfile userProfile);
}
