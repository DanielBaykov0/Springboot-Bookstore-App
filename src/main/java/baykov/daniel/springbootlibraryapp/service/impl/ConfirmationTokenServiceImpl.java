package baykov.daniel.springbootlibraryapp.service.impl;

import baykov.daniel.springbootlibraryapp.entity.ConfirmationToken;
import baykov.daniel.springbootlibraryapp.exception.LibraryHTTPException;
import baykov.daniel.springbootlibraryapp.repository.ConfirmationTokenRepository;
import baykov.daniel.springbootlibraryapp.repository.UserRepository;
import baykov.daniel.springbootlibraryapp.service.ConfirmationTokenService;
import baykov.daniel.springbootlibraryapp.utils.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;

    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository confirmationTokenRepository, UserRepository userRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    @Override
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new LibraryHTTPException(HttpStatus.NOT_FOUND, Messages.TOKEN_NOT_FOUND_MESSAGE));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.EMAIL_ALREADY_CONFIRMED_MESSAGE);
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.TOKEN_EXPIRED_MESSAGE);
        }

        setConfirmationDate(token);
        userRepository.confirmEmail(confirmationToken.getUser().getEmail());
        return Messages.EMAIL_CONFIRMED_SUCCESSFULLY_MESSAGE;
    }

    @Override
    public ConfirmationToken confirmResetToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new LibraryHTTPException(HttpStatus.NOT_FOUND, Messages.TOKEN_NOT_FOUND_MESSAGE));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.EMAIL_ALREADY_CONFIRMED_MESSAGE);
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, Messages.TOKEN_EXPIRED_MESSAGE);
        }

        setConfirmationDate(token);
        return confirmationToken;
    }

    @Override
    public void setConfirmationDate(String token) {
        confirmationTokenRepository.updateConfirmAt(token, LocalDateTime.now());
    }
}
