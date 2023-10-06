package baykov.daniel.springbootbookstoreapp.repository;

import baykov.daniel.springbootbookstoreapp.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b " +
            "JOIN b.authors a " +
            "JOIN b.categories c " +
            "WHERE (:title IS NULL OR LOWER(b.title) LIKE CONCAT('%', LOWER(:title), '%')) " +
            "AND (:authorName IS NULL OR LOWER(CONCAT(a.firstName, ' ', a.lastName)) LIKE CONCAT('%', LOWER(:authorName), '%')) " +
            "AND (:description IS NULL OR LOWER(b.description) LIKE CONCAT('%', LOWER(:description), '%')) " +
            "AND (:category IS NULL OR LOWER(c.name) LIKE CONCAT('%', LOWER(:category), '%')) " +
            "AND (:publicationYear IS NULL OR b.publicationYear = :publicationYear)")
    Page<Book> findBySearchedParams(String title, String authorName, String description, String category, Integer publicationYear, Pageable pageable);
}
