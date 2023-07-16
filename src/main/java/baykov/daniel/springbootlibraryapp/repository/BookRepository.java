package baykov.daniel.springbootlibraryapp.repository;

import baykov.daniel.springbootlibraryapp.entity.Book;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

@Tag(name = "Book Repository")
public interface BookRepository extends JpaRepository<Book, Long> {

    @Operation(
            summary = "Get Books By Title, Genre, Description, Publication year Or Author name",
            description = "Search Books By Title, Genre, Description, Publication year Or Author name " +
                    "is used to get a list of books by title, genre, description, publication year or author name from the database"
    )
    Page<Book> findAllByTitleContainingIgnoreCaseOrGenreContainingIgnoreCaseOrDescriptionIgnoreCaseOrPublicationYearOrAuthorFirstNameIgnoreCaseOrAuthorLastNameIgnoreCase(
            String title, String genre, String description, Long publicationYear, String authorFirstName, String authorLastName, Pageable pageable);
}
