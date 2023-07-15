package baykov.daniel.springbootlibraryapp.service;

import baykov.daniel.springbootlibraryapp.entity.ConfirmationToken;

public interface ConfirmationTokenService {

    void saveConfirmationToken(ConfirmationToken token);

    String confirmToken(String token);

    ConfirmationToken confirmResetToken(String token);

    void setConfirmationDate(String token);
}
