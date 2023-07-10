package baykov.daniel.springbootlibraryapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ebook_history")
public class EBookHistory {

    private Long id;
    private User user;
    private EBook eBook;
    private boolean isRead;
    private boolean isDownloaded;
}
