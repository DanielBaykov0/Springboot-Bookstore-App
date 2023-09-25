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
@Table(name = "ebook")
@PrimaryKeyJoinColumn(name = "product_id")
public class Ebook extends Product {

    @Column(nullable = false)
    private int numberOfPages;

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

    public void update(Ebook ebook, List<Author> authors, List<Category> categories) {
        this.setTitle(ebook.getTitle());
        this.setLanguage(ebook.getLanguage());
        this.setPublicationYear(ebook.getPublicationYear());
        this.setDescription(ebook.getDescription());
        this.setNumberOfPages(ebook.getNumberOfPages());
        this.setISBN(ebook.getISBN());
        this.setFileFormat(ebook.getFileFormat());
        this.setFileSize(ebook.getFileSize());
        this.setPrice(ebook.getPrice());
        this.setAuthors(authors);
        this.setCategories(categories);
    }
}
