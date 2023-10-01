package baykov.daniel.springbootlibraryapp.payload.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VerificationRequestDTO {

    private String code;
}
