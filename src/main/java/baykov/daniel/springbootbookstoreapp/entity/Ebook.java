package baykov.daniel.springbootbookstoreapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ebook")
@PrimaryKeyJoinColumn(name = "product_id")
public class Ebook extends Product {

    @Column(nullable = false)
    @Value(value = "1")
    private Integer numberOfAvailableCopies;

    @Column(nullable = false)
    private Integer numberOfPages;

    @Column(nullable = false)
    private String fileFormat;

    @Column(nullable = false)
    private String fileSize;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "ebook_author",
            joinColumns = @JoinColumn(name = "ebook_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "ebook_category",
            joinColumns = @JoinColumn(name = "ebook_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CommentReview> commentsReviews;

    public Ebook(Ebook ebook, List<Author> authors, List<Category> categories) {
        this.setTitle(ebook.getTitle());
        this.setLanguage(ebook.getLanguage());
        this.setProductType(ProductTypeEnum.EBOOK);
        this.setPublicationYear(ebook.getPublicationYear());
        this.setDescription(ebook.getDescription());
        this.setNumberOfPages(ebook.getNumberOfPages());
        this.setNumberOfAvailableCopies(1);
        this.setISBN(ebook.getISBN());
        this.setFileFormat(ebook.getFileFormat());
        this.setFileSize(ebook.getFileSize());
        this.setPrice(ebook.getPrice());
        this.setAuthors(authors);
        this.setCategories(categories);
    }

    public void update(Ebook ebook, List<Author> authors, List<Category> categories) {
        if (ebook.getTitle() != null) this.setTitle(ebook.getTitle());
        if (ebook.getLanguage() != null) this.setLanguage(ebook.getLanguage());
        this.setProductType(ProductTypeEnum.EBOOK);
        if (ebook.getPublicationYear() != null) this.setPublicationYear(ebook.getPublicationYear());
        if (ebook.getDescription() != null) this.setDescription(ebook.getDescription());
        if (ebook.getNumberOfPages() != null) this.setNumberOfPages(ebook.getNumberOfPages());
        this.setNumberOfAvailableCopies(1);
        if (ebook.getISBN() != null) this.setISBN(ebook.getISBN());
        if (ebook.getFileFormat() != null)  this.setFileFormat(ebook.getFileFormat());
        if (ebook.getFileSize() != null) this.setFileSize(ebook.getFileSize());
        if (ebook.getPrice() != null) this.setPrice(ebook.getPrice());
        if (authors != null) this.setAuthors(authors);
        if (categories != null) this.setCategories(categories);
    }
}
