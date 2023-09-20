package baykov.daniel.springbootlibraryapp.repository;

import baykov.daniel.springbootlibraryapp.entity.Audiobook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudiobookRepository extends JpaRepository<Audiobook, Long> {

}
