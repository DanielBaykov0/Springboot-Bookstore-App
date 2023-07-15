package baykov.daniel.springbootlibraryapp.repositories;

import baykov.daniel.springbootlibraryapp.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findBookByTitleContainingIgnoreCaseOrByGenreContainingIgnoreCaseOrByDescriptionIgnoreCaseOrByPublicationYearOrByAuthorFirstNameIgnoreCaseOrByAuthorLastNameIgnoreCase(
            String title, String genre, String description, int publicationYear, String authorFirstName, String authorLastName, Pageable pageable);
}
