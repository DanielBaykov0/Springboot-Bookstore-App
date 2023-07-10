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
public class BorrowPaperBookHistoryDTO {

    private long id;
    private Long userId;
    private Long paperBookId;
    private LocalDateTime borrowDateTime;
    private LocalDateTime returnDateTime;
    private boolean isReturned;
}
