package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.Token;
import baykov.daniel.springbootbookstoreapp.entity.TokenType;
import baykov.daniel.springbootbookstoreapp.entity.User;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.repository.TokenRepository;
import baykov.daniel.springbootbookstoreapp.repository.TokenTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenTypeServiceTest {

    private TokenTypeService tokenTypeService;

    @Mock
    private TokenTypeRepository tokenTypeRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Captor
    private ArgumentCaptor<Token> tokenArgumentCaptor;

    @Captor
    private ArgumentCaptor<TokenType.TokenTypeEnum> tokenTypeEnumArgumentCaptor;

    @BeforeEach
    void setUp() {
        tokenTypeService = new TokenTypeService(tokenTypeRepository, tokenRepository);
    }

    @Test
    void testCreateNewToken_Success() {
        User user = new User();
        TokenType.TokenTypeEnum tokenTypeEnum = TokenType.TokenTypeEnum.RESET;

        TokenType tokenType = new TokenType();
        when(tokenTypeRepository.findTokenTypeByName(tokenTypeEnum)).thenReturn(Optional.of(tokenType));

        Token token = new Token();
        when(tokenRepository.save(tokenArgumentCaptor.capture())).thenReturn(token);

        String tokenValue = tokenTypeService.createNewToken(user, tokenTypeEnum);
        Assertions.assertNotNull(tokenValue);

        Token capturedToken = tokenArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedToken);
        Assertions.assertEquals(capturedToken.getToken(), tokenValue);
    }

    @Test
    void testCreateNewToken_Exception() {
        User user = new User();
        TokenType.TokenTypeEnum tokenTypeEnum = TokenType.TokenTypeEnum.RESET;

        when(tokenTypeRepository.findTokenTypeByName(tokenTypeEnumArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> tokenTypeService.createNewToken(user, tokenTypeEnum)
        );

        Assertions.assertEquals("TokenType not found with type : 'RESET'", exception.getMessage());

        TokenType.TokenTypeEnum capturedTokenTypeEnum = tokenTypeEnumArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedTokenTypeEnum);
        Assertions.assertEquals(capturedTokenTypeEnum.name(), tokenTypeEnum.name());
    }
}