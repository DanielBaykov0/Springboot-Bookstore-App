package baykov.daniel.springbootbookstoreapp.repository;

import baykov.daniel.springbootbookstoreapp.entity.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenTypeRepository extends JpaRepository<TokenType, Long> {

    Optional<TokenType> findTokenTypeByName(TokenType.TokenTypeEnum tokenTypeName);
}
