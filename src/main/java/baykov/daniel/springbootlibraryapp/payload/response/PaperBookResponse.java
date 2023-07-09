package baykov.daniel.springbootlibraryapp.payload.response;

import baykov.daniel.springbootlibraryapp.payload.dto.PaperBookDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaperBookResponse extends GeneralResponse {

    private List<PaperBookDTO> content;
}
