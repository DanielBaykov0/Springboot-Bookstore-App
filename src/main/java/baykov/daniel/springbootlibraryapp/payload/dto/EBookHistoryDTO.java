package baykov.daniel.springbootlibraryapp.payload.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EBookHistoryDTO {

    private long id;
    private Long userId;
    private Long ebookId;
    private Boolean isRead;
    private Boolean isDownloaded;
}
