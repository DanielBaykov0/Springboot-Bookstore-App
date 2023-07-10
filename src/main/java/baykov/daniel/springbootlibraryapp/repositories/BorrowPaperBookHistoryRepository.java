package baykov.daniel.springbootlibraryapp.repositories;

import baykov.daniel.springbootlibraryapp.entities.BorrowPaperBookHistory;
import baykov.daniel.springbootlibraryapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowPaperBookHistoryRepository extends JpaRepository<BorrowPaperBookHistory, Long> {

    List<BorrowPaperBookHistory> findByUser(User user);
}
