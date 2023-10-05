package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.Token;
import baykov.daniel.springbootbookstoreapp.entity.TokenType;
import baykov.daniel.springbootbookstoreapp.entity.User;
import baykov.daniel.springbootbookstoreapp.exception.LibraryHTTPException;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.repository.TokenRepository;
import baykov.daniel.springbootbookstoreapp.repository.TokenTypeRepository;
import baykov.daniel.springbootbookstoreapp.service.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.TOKEN_TYPE;
import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.TYPE;
import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final TokenTypeRepository tokenTypeRepository;
    private final ServiceUtil serviceUtil;

    @Transactional
    public void validateToken(String token, TokenType.TokenTypeEnum tokenTypeName) {
        log.info("Validating confirmation token for token type: {}", tokenTypeName);
        Token foundToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new LibraryHTTPException(HttpStatus.NOT_FOUND, TOKEN_NOT_FOUND));

        if (foundToken.getConfirmedAt() != null) {
            log.warn("Token validation failed for token type: {}. Email already confirmed.", tokenTypeName);
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, EMAIL_ALREADY_CONFIRMED);
        }

        if (foundToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Token validation failed for token type: {}. Token has expired.", tokenTypeName);
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, TOKEN_EXPIRED);
        }

        if (!foundToken.getTokenType().getName().name().equalsIgnoreCase(tokenTypeName.name())) {
            log.warn("Token validation failed for token type: {}. Invalid token type.", tokenTypeName.name());
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, INVALID_TOKEN_TYPE);
        }

        foundToken.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(foundToken);
        log.info("Token validation successful for token type: {}", tokenTypeName);
    }

    public void checkForPendingTokens(User user, TokenType.TokenTypeEnum tokenTypeName) {
        log.info("Checking for pending tokens for user: {} with token type: {}", user.getEmail(), tokenTypeName);
        TokenType foundToken = tokenTypeRepository.findTokenTypeByName(tokenTypeName)
                .orElseThrow(() -> new ResourceNotFoundException(TOKEN_TYPE, TYPE, tokenTypeName.name()));

        String lastToken = tokenRepository.findLatestTokenByUserIdAndTokenTypeId(user.getId(), foundToken.getId());
        if (lastToken != null) {
            serviceUtil.checkTokenExpired(lastToken);
            log.info("Checked for pending tokens for user: {} with token type: {}. Result: Token expired or not found.", user.getEmail(), tokenTypeName);
        } else {
            log.info("Checked for pending tokens for user: {} with token type: {}. Result: No pending tokens found.", user.getEmail(), tokenTypeName);
        }
    }
}
