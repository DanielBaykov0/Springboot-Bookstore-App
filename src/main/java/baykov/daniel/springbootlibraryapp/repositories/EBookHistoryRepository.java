package baykov.daniel.springbootlibraryapp.repositories;

import baykov.daniel.springbootlibraryapp.entities.EBookHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EBookHistoryRepository extends JpaRepository<EBookHistory, Long> {

    Optional<EBookHistory> findEBookByUserIdAndEBookId(Long userId, Long ebookId);

    Page<EBookHistory> findAllEBooksByUserId(Long userId, Pageable pageable);
}
