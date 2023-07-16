package baykov.daniel.springbootlibraryapp.repository;

import baykov.daniel.springbootlibraryapp.entity.User;
import baykov.daniel.springbootlibraryapp.entity.UserBookHistory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Tag(name = "User Book History Repository")
public interface UserBookHistoryRepository extends JpaRepository<UserBookHistory, Long> {

    List<UserBookHistory> findByUser(User user);

    @Operation(
            summary = "Get Cloud History By Book Id and User Id",
            description = "Search Cloud History By Book Id and User Id is used to get cloud history from the database"
    )
    Optional<UserBookHistory> findByUserIdAndBookId(Long userId, Long bookId);

    @Operation(
            summary = "Get User's Cloud History By User Id",
            description = "Search User's Cloud History By User Id is used to get user's  cloud history from the database"
    )
    Page<UserBookHistory> findAllBooksByUserId(Long userId, Pageable pageable);
}
