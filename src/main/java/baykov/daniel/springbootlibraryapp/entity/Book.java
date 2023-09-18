package baykov.daniel.springbootlibraryapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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
    private Category category;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private int publicationYear;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int numberOfPages;

    @Column(unique = true, nullable = false)
    private String ISBN;

    @Column(nullable = false)
    private int numberOfAvailableCopies;

    @Column(nullable = false)
    private int numberOfTotalCopies;

    @Column(nullable = false)
    private BigDecimal price;

    public void update(Book book, Author author) {
        this.title = book.getTitle();
        this.author = author;
        this.category = book.getCategory();
        this.language = book.getLanguage();
        this.publicationYear = book.getPublicationYear();
        this.description = book.getDescription();
        this.numberOfPages = book.getNumberOfPages();
        this.ISBN = book.getISBN();
        this.numberOfAvailableCopies = book.getNumberOfAvailableCopies();
        this.numberOfTotalCopies = book.getNumberOfTotalCopies();
        this.price = book.getPrice();
    }
}
