package baykov.daniel.springbootlibraryapp.entities;

import baykov.daniel.springbootlibraryapp.utils.AppConstants;
import com.github.ladutsko.isbn.ISBNException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private int publicationYear;
    private String description;

    @Column(unique = true, nullable = false)
    private com.github.ladutsko.isbn.ISBN ISBN;

    {
        try {
            ISBN = com.github.ladutsko.isbn.ISBN.parseIsbn(String.valueOf(ThreadLocalRandom.current().nextLong(AppConstants.ISBN_LOWER_BOUND, AppConstants.ISBN_UPPER_BOUND)));
        } catch (ISBNException e) {
            throw new RuntimeException(e);
        }
    }

    private int numberOfCopiesAvailable;
    private int numberOfCopiesTotal;

    @Column(unique = true)
    private String readingLink;

    @Column(unique = true)
    private String downloadLink;
}
