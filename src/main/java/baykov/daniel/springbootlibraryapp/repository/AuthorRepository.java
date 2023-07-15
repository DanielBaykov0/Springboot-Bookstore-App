package baykov.daniel.springbootlibraryapp.repository;

import baykov.daniel.springbootlibraryapp.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Page<Author> findAllByFirstNameIgnoreCaseOrLastNameIgnoreCaseOrCountryBornIgnoreCase(String firstName, String lastName, String country, Pageable pageable);
}
