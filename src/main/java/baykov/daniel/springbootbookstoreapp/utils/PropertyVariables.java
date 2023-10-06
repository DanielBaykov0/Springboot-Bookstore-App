package baykov.daniel.springbootbookstoreapp.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class PropertyVariables {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${redirect.uri}")
    private String redirectUri;

    @Value("${confirm.email.uri}")
    private String confirmEmailUri;

    @Value("${reset.password.uri}")
    private String resetPasswordUri;
}
