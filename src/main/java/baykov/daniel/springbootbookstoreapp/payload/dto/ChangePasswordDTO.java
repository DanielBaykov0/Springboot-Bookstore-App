package baykov.daniel.springbootbookstoreapp.payload.dto;

import baykov.daniel.springbootbookstoreapp.validator.password.PasswordValueMatches;
import baykov.daniel.springbootbookstoreapp.validator.password.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@PasswordValueMatches.List({
        @PasswordValueMatches(
                field = "password",
                fieldMatch = "matchingPassword"
        )
})
public class ChangePasswordDTO {

    @Schema(description = "New password", example = "!NewPassword123")
    @NotEmpty(message = "Password should not be null or empty")
    @ValidPassword
    String password;

    @Schema(description = "Matching password for confirmation", example = "!NewPassword123")
    @NotEmpty(message = "Matching Password should not be null or empty")
    @ValidPassword
    String matchingPassword;
}
