package baykov.daniel.springbootlibraryapp.repository;

import baykov.daniel.springbootlibraryapp.entity.User;
import baykov.daniel.springbootlibraryapp.entity.UserBookHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBookHistoryRepository extends JpaRepository<UserBookHistory, Long> {

    List<UserBookHistory> findByUser(User user);

    Optional<UserBookHistory> findEBookByUserIdAndBookId(Long userId, Long bookId);

    Page<UserBookHistory> findAllEBooksByUserId(Long userId, Pageable pageable);
}
