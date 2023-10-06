package baykov.daniel.springbootbookstoreapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CommentReview> commentsReviews;

    public Book(Book book, List<Author> authors, List<Category> categories) {
        this.setTitle(book.getTitle());
        this.setLanguage(book.getLanguage());
        this.setProductType(ProductTypeEnum.BOOK);
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

    public void update(Book book, List<Author> authors, List<Category> categories) {
        if (book.getTitle() != null) this.setTitle(book.getTitle());
        if (book.getLanguage() != null) this.setLanguage(book.getLanguage());
        this.setProductType(ProductTypeEnum.BOOK);
        if (book.getPublicationYear() != null) this.setPublicationYear(book.getPublicationYear());
        if (book.getDescription() != null) this.setDescription(book.getDescription());
        if (book.getNumberOfPages() != null) this.setNumberOfPages(book.getNumberOfPages());
        if (book.getISBN() != null) this.setISBN(book.getISBN());
        if (book.getNumberOfAvailableCopies() != null) this.setNumberOfAvailableCopies(book.getNumberOfAvailableCopies());
        if (book.getNumberOfTotalCopies() != null) this.setNumberOfTotalCopies(book.getNumberOfTotalCopies());
        if (book.getPrice() != null) this.setPrice(book.getPrice());
        if (authors != null) this.setAuthors(authors);
        if (categories != null) this.setCategories(categories);
    }
}
