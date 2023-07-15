package baykov.daniel.springbootlibraryapp.payload.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDTO {

    @NotEmpty(message = "Password should not be null or empty")
    String oldPassword;

    @NotEmpty(message = "Password should not be null or empty")
    @Size(min = 8, max = 20, message = "Password should be between 8 and 20 characters")
    String newPassword;

    @NotEmpty(message = "Password should not be null or empty")
    @Size(min = 8, max = 20, message = "Password should be between 8 and 20 characters")
    String confirmNewPassword;
}
