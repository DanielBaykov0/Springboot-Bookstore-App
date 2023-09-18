package baykov.daniel.springbootlibraryapp.payload.response;

import baykov.daniel.springbootlibraryapp.payload.dto.AudiobookDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AudiobookResponse extends BaseResponse {

    private List<AudiobookDTO> content;
}
