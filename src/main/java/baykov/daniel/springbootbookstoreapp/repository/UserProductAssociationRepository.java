package baykov.daniel.springbootbookstoreapp.repository;

import baykov.daniel.springbootbookstoreapp.entity.UserProductAssociation;
import baykov.daniel.springbootbookstoreapp.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProductAssociationRepository extends JpaRepository<UserProductAssociation, Long> {

    List<UserProductAssociation> findByUserProfile(UserProfile userProfile);
}
