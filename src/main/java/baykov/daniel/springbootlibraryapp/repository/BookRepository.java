package baykov.daniel.springbootlibraryapp.repository;

import baykov.daniel.springbootlibraryapp.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT b from Book b " +
            "JOIN Author a ON b.author_id = a.id " +
            "WHERE (:name IS NULL OR CONCAT(LOWER(a.firstName), ' ', LOWER(a.lastName)) LIKE %:name%)")
    Page<Book> findAllByAuthorName(String name, Pageable pageable);

    Page<Book> findAllByDescriptionContainingIgnoreCase(String description, Pageable pageable);

    Page<Book> findAllByGenreContainingIgnoreCase(String genre, Pageable pageable);

    Page<Book> findAllByPublicationYear(int publicationYear, Pageable pageable);
}
