package baykov.daniel.springbootlibraryapp.payload.response;

import baykov.daniel.springbootlibraryapp.payload.dto.EBookDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EBookResponse extends GeneralResponse {

    private List<EBookDTO> content;
}
