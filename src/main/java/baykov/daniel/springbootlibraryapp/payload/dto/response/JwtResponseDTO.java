package baykov.daniel.springbootlibraryapp.payload.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JwtResponseDTO {

    private String accessToken;
    private String refreshToken;
    private String secretImageUri;
}
