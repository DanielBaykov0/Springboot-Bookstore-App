package baykov.daniel.springbootlibraryapp.repository;

import baykov.daniel.springbootlibraryapp.entity.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenTypeRepository extends JpaRepository<TokenType, Long> {

    Optional<TokenType> findTokenTypeByName(TokenType.TokenTypeEnum tokenTypeName);
}
