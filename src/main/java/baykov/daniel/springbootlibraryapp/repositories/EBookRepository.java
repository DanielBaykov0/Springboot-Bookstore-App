package baykov.daniel.springbootlibraryapp.repositories;

import baykov.daniel.springbootlibraryapp.entities.EBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EBookRepository extends JpaRepository<EBook, Long> {
}
