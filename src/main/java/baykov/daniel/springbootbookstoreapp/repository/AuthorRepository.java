package baykov.daniel.springbootbookstoreapp.repository;

import baykov.daniel.springbootbookstoreapp.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a from Author a " +
            "WHERE (:name IS NULL OR CONCAT(LOWER(a.firstName), ' ', LOWER(a.lastName)) LIKE CONCAT('%', :name, '%')) " +
            "AND (:countryId IS NULL OR a.country.id = :countryId) " +
            "AND (:cityId IS NULL OR a.city.id = :cityId)")
    Page<Author> findBySearchParams(String name, Long countryId, Long cityId, Pageable pageable);
}
