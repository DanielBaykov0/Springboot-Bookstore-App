package baykov.daniel.springbootlibraryapp.payload.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordDTO {

    private String usernameOrEmail;

    @Size(min = 8, max = 20, message = "Password should be between 8 and 20 characters")
    String newPassword;

    String confirmNewPassword;
}
