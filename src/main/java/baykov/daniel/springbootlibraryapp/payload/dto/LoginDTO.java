package baykov.daniel.springbootlibraryapp.payload.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    @NotEmpty(message = "Email should not be null or empty")
    private String email;

    @NotEmpty(message = "Password should not be null or empty")
    private String password;
}
