package baykov.daniel.springbootbookstoreapp.repository;

import baykov.daniel.springbootbookstoreapp.entity.Ebook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EbookRepository extends JpaRepository<Ebook, Long> {

    @Query("SELECT e FROM Ebook e " +
            "JOIN e.authors a " +
            "JOIN e.categories c " +
            "WHERE (:title IS NULL OR LOWER(e.title) LIKE CONCAT('%', LOWER(:title), '%')) " +
            "AND (:authorName IS NULL OR LOWER(CONCAT(a.firstName, ' ', a.lastName)) LIKE CONCAT('%', LOWER(:authorName), '%')) " +
            "AND (:description IS NULL OR LOWER(e.description) LIKE CONCAT('%', LOWER(:description), '%')) " +
            "AND (:category IS NULL OR LOWER(c.name) LIKE CONCAT('%', LOWER(:category), '%')) " +
            "AND (:publicationYear IS NULL OR e.publicationYear = :publicationYear)")
    Page<Ebook> findBySearchedParams(String title, String authorName, String description, String category, Integer publicationYear, Pageable pageable);
}
