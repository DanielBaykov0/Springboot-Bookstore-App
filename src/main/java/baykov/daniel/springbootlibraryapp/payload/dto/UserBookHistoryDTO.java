package baykov.daniel.springbootlibraryapp.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBookHistoryDTO {

    private long id;
    private Long userId;
    private Long bookId;
    private LocalDateTime borrowDateTime;
    private LocalDateTime returnDateTime;
    private Boolean isReturned;
    private Boolean isRead;
    private Boolean isDownloaded;
}
