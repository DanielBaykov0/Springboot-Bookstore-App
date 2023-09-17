package baykov.daniel.springbootlibraryapp.repository;

import baykov.daniel.springbootlibraryapp.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Page<Author> findAllByCountryId(Long countryId, Pageable pageable);

    Page<Author> findAllByCityId(Long cityId, Pageable pageable);

    @Query("SELECT a from Author a " +
            "WHERE (:name IS NULL OR CONCAT(LOWER(a.firstName), ' ', LOWER(a.lastName)) LIKE %:name%) " +
            "AND (:country IS NULL OR LOWER(a.country) LIKE %:country%) " +
            "AND (:city IS NULL OR LOWER(a.city) LIKE %:city%)")
    Page<Author> findBySearchParams(String name, String country, String city, Pageable pageable);
}
