package baykov.daniel.springbootlibraryapp.payload.response;

import baykov.daniel.springbootlibraryapp.payload.dto.BorrowPaperBookHistoryDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BorrowPaperBookHistoryResponse extends GeneralResponse {

    private List<BorrowPaperBookHistoryDTO> content;
}
