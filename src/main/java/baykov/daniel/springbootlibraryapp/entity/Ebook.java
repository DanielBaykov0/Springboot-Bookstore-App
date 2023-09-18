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
@Table(name = "ebook")
public class Ebook extends BaseEntity {

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

    private Boolean isDownloadable;

    @Column(nullable = false)
    private String fileFormat;

    @Column(nullable = false)
    private String fileSize;

    @Column(nullable = false)
    private BigDecimal price;

    public void update(Ebook ebook, Author author) {
        this.title = ebook.getTitle();
        this.author = author;
        this.category = ebook.getCategory();
        this.language = ebook.getLanguage();
        this.publicationYear = ebook.getPublicationYear();
        this.description = ebook.getDescription();
        this.numberOfPages = ebook.getNumberOfPages();
        this.ISBN = ebook.getISBN();
        this.isDownloadable = ebook.getIsDownloadable();
        this.fileFormat = ebook.getFileFormat();
        this.fileSize = ebook.getFileSize();
        this.price = ebook.getPrice();
    }
}
