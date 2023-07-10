package baykov.daniel.springbootlibraryapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "borrow_history")
public class BorrowPaperBookHistory {

    private long id;
    private User user;
    private PaperBook paperBook;
    private LocalDateTime borrowDateTime;
    private LocalDateTime returnDateTime;
    private boolean isReturned;
}
