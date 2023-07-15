package baykov.daniel.springbootlibraryapp.repositories;

import baykov.daniel.springbootlibraryapp.entities.UserBookHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EBookHistoryRepository extends JpaRepository<UserBookHistory, Long> {

    Optional<UserBookHistory> findEBookByUserIdAndEBookId(Long userId, Long ebookId);

    Page<UserBookHistory> findAllEBooksByUserId(Long userId, Pageable pageable);
}
