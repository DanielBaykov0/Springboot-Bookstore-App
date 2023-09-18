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
@Table(name = "audiobook")
public class Audiobook extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false, name = "author_id")
    private Author author;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false, name = "narrator_id")
    private Narrator narrator;

    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private int publicationYear;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal duration;

    @Column(unique = true, nullable = false)
    private String ISBN;

    @Column(nullable = false)
    private Book isDownloadable;

    @Column(nullable = false)
    private String fileFormat;

    @Column(nullable = false)
    private String fileSize;

    @Column(nullable = false)
    private BigDecimal price;

    public void update(Audiobook audiobook, Author author, Narrator narrator) {
        this.title = audiobook.getTitle();
        this.author = author;
        this.narrator = narrator;
        this.category = audiobook.getCategory();
        this.language = audiobook.getLanguage();
        this.publicationYear = audiobook.getPublicationYear();
        this.description = audiobook.getDescription();
        this.duration = audiobook.getDuration();
        this.ISBN = audiobook.getISBN();
        this.isDownloadable = audiobook.getIsDownloadable();
        this.fileFormat = audiobook.getFileFormat();
        this.fileSize = audiobook.getFileSize();
        this.price = audiobook.getPrice();
    }
}
