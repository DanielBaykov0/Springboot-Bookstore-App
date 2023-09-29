package baykov.daniel.springbootlibraryapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "audiobook")
@PrimaryKeyJoinColumn(name = "product_id")
public class Audiobook extends Product {

    @Column(nullable = false)
    @Value(value = "1")
    private Integer numberOfAvailableCopies;

    @Column(nullable = false)
    private BigDecimal duration;

    @Column(nullable = false)
    private String fileFormat;

    @Column(nullable = false)
    private String fileSize;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "audiobook_author",
            joinColumns = @JoinColumn(name = "audiobook_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "audiobook_category",
            joinColumns = @JoinColumn(name = "audiobook_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(nullable = false, name = "narrator_id")
    private Narrator narrator;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CommentReview> commentsReviews;

    public Audiobook(Audiobook audiobook, List<Author> authors, List<Category> categories, Narrator narrator) {
        this.setTitle(audiobook.getTitle());
        this.setLanguage(audiobook.getLanguage());
        this.setProductType(ProductTypeEnum.AUDIOBOOK);
        this.setPublicationYear(audiobook.getPublicationYear());
        this.setDescription(audiobook.getDescription());
        this.setNumberOfAvailableCopies(1);
        this.setDuration(audiobook.getDuration());
        this.setISBN(audiobook.getISBN());
        this.setFileFormat(audiobook.getFileFormat());
        this.setFileSize(audiobook.getFileSize());
        this.setPrice(audiobook.getPrice());
        this.setAuthors(authors);
        this.setCategories(categories);
        this.setNarrator(narrator);
    }

    public void update(Audiobook audiobook, List<Author> authors, List<Category> categories, Narrator narrator) {
        if (audiobook.getTitle() != null) this.setTitle(audiobook.getTitle());
        if (audiobook.getLanguage() != null) this.setLanguage(audiobook.getLanguage());
        this.setProductType(ProductTypeEnum.AUDIOBOOK);
        if (audiobook.getPublicationYear() != null) this.setPublicationYear(audiobook.getPublicationYear());
        if (audiobook.getDescription() != null) this.setDescription(audiobook.getDescription());
        this.setNumberOfAvailableCopies(1);
        if (audiobook.getDuration() != null) this.setDuration(audiobook.getDuration());
        if (audiobook.getISBN() != null) this.setISBN(audiobook.getISBN());
        if (audiobook.getFileFormat() != null) this.setFileFormat(audiobook.getFileFormat());
        if (audiobook.getFileSize() != null) this.setFileSize(audiobook.getFileSize());
        if (audiobook.getPrice() != null) this.setPrice(audiobook.getPrice());
        if (authors != null) this.setAuthors(authors);
        if (categories != null) this.setCategories(categories);
        if (narrator != null) this.setNarrator(narrator);
    }
}
