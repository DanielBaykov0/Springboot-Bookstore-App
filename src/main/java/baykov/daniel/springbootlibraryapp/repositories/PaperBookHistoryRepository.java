package baykov.daniel.springbootlibraryapp.repositories;

import baykov.daniel.springbootlibraryapp.entities.PaperBookHistory;
import baykov.daniel.springbootlibraryapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaperBookHistoryRepository extends JpaRepository<PaperBookHistory, Long> {

    List<PaperBookHistory> findByUser(User user);
}
