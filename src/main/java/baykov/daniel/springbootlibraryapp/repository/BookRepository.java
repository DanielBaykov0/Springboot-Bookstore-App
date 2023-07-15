package baykov.daniel.springbootlibraryapp.repository;

import baykov.daniel.springbootlibraryapp.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findAllByTitleContainingIgnoreCaseOrGenreContainingIgnoreCaseOrDescriptionIgnoreCaseOrPublicationYearOrAuthorFirstNameIgnoreCaseOrAuthorLastNameIgnoreCase(
            String title, String genre, String description, Long publicationYear, String authorFirstName, String authorLastName, Pageable pageable);
}
