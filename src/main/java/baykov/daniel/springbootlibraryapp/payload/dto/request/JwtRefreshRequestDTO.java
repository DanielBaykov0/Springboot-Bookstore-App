package baykov.daniel.springbootlibraryapp.payload.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class JwtRefreshRequestDTO {

    @NotEmpty
    private String refreshToken;
}
