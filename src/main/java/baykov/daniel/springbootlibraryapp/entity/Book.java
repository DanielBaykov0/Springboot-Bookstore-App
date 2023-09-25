package baykov.daniel.springbootlibraryapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
@PrimaryKeyJoinColumn(name = "product_id")
public class Book extends Product {

    @Column(nullable = false)
    private Integer numberOfPages;

    @Column(nullable = false)
    private Integer numberOfAvailableCopies;

    @Column(nullable = false)
    private Integer numberOfTotalCopies;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    public void update(Book book, List<Author> authors, List<Category> categories) {
        this.setTitle(book.getTitle());
        this.setLanguage(book.getLanguage());
        this.setPublicationYear(book.getPublicationYear());
        this.setDescription(book.getDescription());
        this.setNumberOfPages(book.getNumberOfPages());
        this.setISBN(book.getISBN());
        this.setNumberOfAvailableCopies(book.getNumberOfAvailableCopies());
        this.setNumberOfTotalCopies(book.getNumberOfTotalCopies());
        this.setPrice(book.getPrice());
        this.setAuthors(authors);
        this.setCategories(categories);
    }
}
