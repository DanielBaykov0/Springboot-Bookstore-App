package baykov.daniel.springbootbookstoreapp.payload.dto.request;

import baykov.daniel.springbootbookstoreapp.validator.email.ValidEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserProfileEmailRequestDTO {

    @Schema(description = "Email address", example = "example@example.com")
    @ValidEmail
    private String email;
}
