package baykov.daniel.springbootlibraryapp.payload.dto;

import baykov.daniel.springbootlibraryapp.validator.email.ValidEmail;
import baykov.daniel.springbootlibraryapp.validator.password.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginDTO {

    @NotEmpty(message = "Email should not be null or empty")
    @Schema(example = "example@example.com")
    @ValidEmail
    private String email;

    @NotEmpty(message = "Password should not be null or empty")
    @Schema(example = "!Password123")
    @ValidPassword
    private String password;
}
