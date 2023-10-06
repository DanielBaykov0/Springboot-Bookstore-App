package baykov.daniel.springbootbookstoreapp.repository;

import baykov.daniel.springbootbookstoreapp.entity.Narrator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NarratorRepository extends JpaRepository<Narrator, Long> {

    @Query("SELECT n FROM Narrator n " +
            "WHERE (:name IS NULL OR CONCAT(LOWER(n.firstName), ' ', LOWER(n.lastName)) LIKE CONCAT('%', :name, '%')) " +
            "AND (:countryId IS NULL OR n.country.id = :countryId) " +
            "AND (:cityId IS NULL OR n.city.id = :cityId)")
    Page<Narrator> findBySearchParams(String name, Long countryId, Long cityId, Pageable pageable);
}
