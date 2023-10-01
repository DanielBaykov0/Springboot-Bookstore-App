package baykov.daniel.springbootlibraryapp.payload.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JwtRefreshResponseDTO {

    private String accessToken;
    private String refreshToken;
}
