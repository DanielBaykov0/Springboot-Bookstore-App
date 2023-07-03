package baykov.daniel.springbootlibraryapp.entities;

import baykov.daniel.springbootlibraryapp.utils.AppConstants;
import com.github.ladutsko.isbn.ISBNException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "paper_books")
public class PaperBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "default 'PAPER'")
    private BookType bookType;

    @Column(nullable = false)
    private String bookTitle;

    @Column(nullable = false)
    private String bookAuthor;

    @Column(nullable = false)
    private String bookGenre;
    private String bookDescription;

    @Column(unique = true, nullable = false)
    private com.github.ladutsko.isbn.ISBN ISBN;

    {
        try {
            ISBN = com.github.ladutsko.isbn.ISBN.parseIsbn(String.valueOf(ThreadLocalRandom.current().nextLong(AppConstants.ISBN_LOWER_BOUND, AppConstants.ISBN_UPPER_BOUND)));
        } catch (ISBNException e) {
            throw new RuntimeException(e);
        }
    }

    @Column(nullable = false)
    private int paperBookNumberOfCopiesAvailable;

    @Column(nullable = false)
    private int paperBookNumberOfCopiesTotal;

    @Column(nullable = false)
    private LocalDateTime borrowedDate;
}
