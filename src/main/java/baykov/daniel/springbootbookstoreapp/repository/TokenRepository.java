package baykov.daniel.springbootbookstoreapp.repository;

import baykov.daniel.springbootbookstoreapp.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    @Query(value = "SELECT user_id FROM token WHERE token=?1", nativeQuery = true)
    Long findUserIdByToken(String token);

    @Query("SELECT t FROM Token t WHERE t.user.id = :userId AND t.tokenType.id = :tokenTypeId ORDER BY t.createdAt DESC LIMIT 1")
    String findLatestTokenByUserIdAndTokenTypeId(Long userId, Long tokenTypeId);
}
