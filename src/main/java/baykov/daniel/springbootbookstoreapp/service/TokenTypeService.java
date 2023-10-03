package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.Token;
import baykov.daniel.springbootbookstoreapp.entity.TokenType;
import baykov.daniel.springbootbookstoreapp.entity.User;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.repository.TokenRepository;
import baykov.daniel.springbootbookstoreapp.repository.TokenTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.TOKEN_TYPE;
import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.TYPE;

@Component
@RequiredArgsConstructor
public class TokenTypeService {

    private final TokenTypeRepository tokenTypeRepository;
    private final TokenRepository tokenRepository;

    @Transactional
    public String createNewToken(User user, TokenType.TokenTypeEnum tokenTypeName) {
        TokenType tokenType = tokenTypeRepository.findTokenTypeByName(tokenTypeName)
                .orElseThrow(() -> new ResourceNotFoundException(TOKEN_TYPE, TYPE, tokenTypeName.name()));

        Token token = new Token(user, tokenType);
        tokenRepository.save(token);
        return token.getToken();
    }
}
