package baykov.daniel.springbootlibraryapp.entities;

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
    private String ISBN;

    private int numberOfCopiesAvailable;
    private int numberOfCopiesTotal;

    @Column(unique = true)
    private String readingLink;

    @Column(unique = true)
    private String downloadLink;
}
