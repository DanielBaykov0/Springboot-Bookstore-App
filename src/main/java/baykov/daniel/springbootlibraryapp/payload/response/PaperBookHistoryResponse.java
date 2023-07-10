package baykov.daniel.springbootlibraryapp.payload.response;

import baykov.daniel.springbootlibraryapp.payload.dto.PaperBookHistoryDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaperBookHistoryResponse extends GeneralResponse {

    private List<PaperBookHistoryDTO> content;
}
