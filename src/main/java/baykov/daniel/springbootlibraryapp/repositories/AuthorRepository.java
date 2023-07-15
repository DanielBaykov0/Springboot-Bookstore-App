package baykov.daniel.springbootlibraryapp.repositories;

import baykov.daniel.springbootlibraryapp.entities.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Page<Author> findAllByAuthorFirstNameIgnoreCaseOrAuthorLastNameIgnoreCaseOrAuthorCountryIgnoreCase(String firstName, String lastName, String country, Pageable pageable);
}
