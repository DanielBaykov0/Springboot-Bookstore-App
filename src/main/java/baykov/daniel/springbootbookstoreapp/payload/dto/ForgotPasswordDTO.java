package baykov.daniel.springbootbookstoreapp.payload.dto;

import baykov.daniel.springbootbookstoreapp.validator.email.ValidEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ForgotPasswordDTO {

    @Schema(description = "Email address", example = "example@example.com")
    @ValidEmail
    private String email;
}
