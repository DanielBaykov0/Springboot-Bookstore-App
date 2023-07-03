package baykov.daniel.springbootlibraryapp.repositories;

import baykov.daniel.springbootlibraryapp.entities.PaperBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaperBookRepository extends JpaRepository<PaperBook, Long> {
}
