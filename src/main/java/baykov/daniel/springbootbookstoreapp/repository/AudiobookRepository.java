package baykov.daniel.springbootbookstoreapp.repository;

import baykov.daniel.springbootbookstoreapp.entity.Audiobook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AudiobookRepository extends JpaRepository<Audiobook, Long> {

    @Query("SELECT ab FROM Audiobook ab " +
            "JOIN ab.authors a " +
            "JOIN ab.categories c " +
            "WHERE (:title IS NULL OR LOWER(ab.title) LIKE CONCAT('%', LOWER(:title), '%')) " +
            "AND (:authorName IS NULL OR LOWER(CONCAT(a.firstName, ' ', a.lastName)) LIKE CONCAT('%', LOWER(:authorName), '%')) " +
            "AND (:description IS NULL OR LOWER(ab.description) LIKE CONCAT('%', LOWER(:description), '%')) " +
            "AND (:category IS NULL OR LOWER(c.name) LIKE CONCAT('%', LOWER(:category), '%')) " +
            "AND (:publicationYear IS NULL OR ab.publicationYear = :publicationYear)")
    Page<Audiobook> findBySearchedParams(String title, String authorName, String description, String category, Integer publicationYear, Pageable pageable);
}
