package baykov.daniel.springbootlibraryapp.payload.response;

import baykov.daniel.springbootlibraryapp.payload.dto.UserBookHistoryDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserBookHistoryResponse extends GeneralResponse {

    private List<UserBookHistoryDTO> content;
}
