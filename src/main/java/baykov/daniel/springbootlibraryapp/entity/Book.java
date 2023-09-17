package baykov.daniel.springbootlibraryapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class Book extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false, name = "author_id")
    private Author author;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private int publicationYear;

    private String description;

    @Column(unique = true, nullable = false)
    private String ISBN;

    @Column(nullable = false)
    private int numberOfAvailableCopies;

    @Column(nullable = false)
    private int numberOfTotalCopies;

    @Column(unique = true)
    private String readingLink;

    @Column(unique = true)
    private String downloadLink;

    public void update(Book book, Author author) {
        this.title = book.getTitle();
        this.author = author;
        this.genre = book.getGenre();
        this.publicationYear = book.getPublicationYear();
        this.description = book.getDescription();
        this.ISBN = book.getISBN();
        this.numberOfAvailableCopies = book.getNumberOfAvailableCopies();
        this.numberOfTotalCopies = book.getNumberOfTotalCopies();
        this.readingLink = book.getReadingLink();
        this.downloadLink = book.getDownloadLink();
    }
}
