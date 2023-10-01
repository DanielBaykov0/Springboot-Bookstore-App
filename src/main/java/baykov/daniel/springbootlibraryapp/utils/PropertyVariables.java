package baykov.daniel.springbootlibraryapp.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PropertyVariables {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    @Value("${app.jwtRefreshExpiration}")
    private Long refreshTokenDurationMs;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${redirect.uri}")
    private String redirectUri;

    @Value("${confirm.email.uri}")
    private String confirmEmailUri;

    @Value("${reset.password.uri}")
    private String resetPasswordUri;

    @Value("${api.publishable.test.key}")
    private String stripePublishableTestKey;

    @Value("${api.secret.test.key}")
    private String stripeSecretTestKey;
}
