package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.Token;
import baykov.daniel.springbootbookstoreapp.entity.TokenType;
import baykov.daniel.springbootbookstoreapp.entity.User;
import baykov.daniel.springbootbookstoreapp.exception.LibraryHTTPException;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.repository.TokenRepository;
import baykov.daniel.springbootbookstoreapp.repository.TokenTypeRepository;
import baykov.daniel.springbootbookstoreapp.service.util.ServiceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    private TokenService tokenService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private TokenTypeRepository tokenTypeRepository;

    @Mock
    private ServiceUtil serviceUtil;

    @Captor
    private ArgumentCaptor<Token> tokenArgumentCaptor;

    @Captor
    private ArgumentCaptor<TokenType.TokenTypeEnum> tokenTypeEnumArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService(tokenRepository, tokenTypeRepository, serviceUtil);
    }

    @Test
    void testValidateToken_Success() {
        String token = "validToken";
        TokenType tokenType = new TokenType(TokenType.TokenTypeEnum.CONFIRMATION);
        TokenType.TokenTypeEnum tokenTypeEnum = TokenType.TokenTypeEnum.CONFIRMATION;

        Token foundToken = new Token();
        foundToken.setExpiresAt(LocalDateTime.now().plusHours(1));
        foundToken.setTokenType(tokenType);
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(foundToken));

        when(tokenRepository.save(tokenArgumentCaptor.capture())).thenReturn(foundToken);

        tokenService.validateToken(token, tokenTypeEnum);

        Token capturedToken = tokenArgumentCaptor.getValue();
        assertNotNull(capturedToken.getConfirmedAt());
        Assertions.assertEquals(tokenType, capturedToken.getTokenType());
        Assertions.assertEquals(tokenTypeEnum, capturedToken.getTokenType().getName());
    }

    @Test
    void testValidateToken_ReturnTokenNotFoundException() {
        String token = "validToken";
        TokenType.TokenTypeEnum tokenTypeEnum = TokenType.TokenTypeEnum.CONFIRMATION;

        when(tokenRepository.findByToken(stringArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> tokenService.validateToken(token, tokenTypeEnum)
        );

        Assertions.assertEquals("Token not found with value : 'validToken'", exception.getMessage());

        String capturedToken = stringArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedToken);
        Assertions.assertEquals(capturedToken, token);
    }

    @Test
    void testValidateToken_ReturnTokenConfirmedException() {
        String token = "validToken";
        TokenType tokenType = new TokenType(TokenType.TokenTypeEnum.CONFIRMATION);
        TokenType.TokenTypeEnum tokenTypeEnum = TokenType.TokenTypeEnum.CONFIRMATION;

        Token foundToken = new Token();
        foundToken.setExpiresAt(LocalDateTime.now().plusHours(1));
        foundToken.setTokenType(tokenType);
        foundToken.setConfirmedAt(LocalDateTime.now().minusHours(1));
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(foundToken));

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> tokenService.validateToken(token, tokenTypeEnum)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(EMAIL_ALREADY_CONFIRMED, exception.getMessage());
    }

    @Test
    void testValidateToken_ReturnTokenExpiredException() {
        String token = "validToken";
        TokenType tokenType = new TokenType(TokenType.TokenTypeEnum.CONFIRMATION);
        TokenType.TokenTypeEnum tokenTypeEnum = TokenType.TokenTypeEnum.CONFIRMATION;

        Token foundToken = new Token();
        foundToken.setExpiresAt(LocalDateTime.now().minusHours(1));
        foundToken.setTokenType(tokenType);
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(foundToken));

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> tokenService.validateToken(token, tokenTypeEnum)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(TOKEN_EXPIRED, exception.getMessage());
    }

    @Test
    void testCheckForPendingTokens_Success() {
        User user = new User();
        user.setId(1L);
        TokenType.TokenTypeEnum tokenTypeEnum = TokenType.TokenTypeEnum.CONFIRMATION;

        TokenType tokenType = new TokenType();
        tokenType.setId(1L);
        when(tokenTypeRepository.findTokenTypeByName(tokenTypeEnum)).thenReturn(Optional.of(tokenType));

        String tokenValue = "";
        when(tokenRepository.findLatestTokenByUserIdAndTokenTypeId(eq(user.getId()), longArgumentCaptor.capture())).thenReturn(tokenValue);

        tokenService.checkForPendingTokens(user, tokenTypeEnum);

        Long capturedTokenTypeId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedTokenTypeId);
        Assertions.assertEquals(capturedTokenTypeId, tokenType.getId());
    }

    @Test
    void testCheckForPendingTokens_ReturnTokenTypeException() {
        User user = new User();
        TokenType.TokenTypeEnum tokenTypeEnum = TokenType.TokenTypeEnum.CONFIRMATION;

        when(tokenTypeRepository.findTokenTypeByName(tokenTypeEnumArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> tokenService.checkForPendingTokens(user, tokenTypeEnum)
        );

        Assertions.assertEquals("TokenType not found with type : 'CONFIRMATION'", exception.getMessage());

        TokenType.TokenTypeEnum capturedTokenTypeEnum = tokenTypeEnumArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedTokenTypeEnum);
        Assertions.assertEquals(capturedTokenTypeEnum, tokenTypeEnum);
    }
}