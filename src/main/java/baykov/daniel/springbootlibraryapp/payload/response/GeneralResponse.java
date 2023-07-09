package baykov.daniel.springbootlibraryapp.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralResponse {

    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
