package baykov.daniel.springbootlibraryapp.repositories;

import baykov.daniel.springbootlibraryapp.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaperBookRepository extends JpaRepository<Book, Long> {
}
