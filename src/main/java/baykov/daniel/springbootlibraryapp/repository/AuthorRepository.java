package baykov.daniel.springbootlibraryapp.repository;

import baykov.daniel.springbootlibraryapp.entity.Author;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

@Tag(name = "Author Repository")
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Operation(
            summary = "Get Author By First Name, Last Name Or Country",
            description = "Get Author By First Name, Last Name Or Country is used to get all authors by first name, last name or country from the database"
    )
    Page<Author> findAllByFirstNameIgnoreCaseOrLastNameIgnoreCaseOrCountryBornIgnoreCase(String firstName, String lastName, String country, Pageable pageable);
}
