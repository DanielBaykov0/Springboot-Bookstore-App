package baykov.daniel.springbootlibraryapp.payload.response;

import baykov.daniel.springbootlibraryapp.payload.dto.AuthorDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthorResponse extends GeneralResponse {

    List<AuthorDTO> content;
}
