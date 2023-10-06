package baykov.daniel.springbootbookstoreapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "product")
@Inheritance(strategy = InheritanceType.JOINED)
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private Integer publicationYear;

    @Column(nullable = false)
    private String description;

    @Column(unique = true, nullable = false)
    private String ISBN;

    @Column(name = "avg_rating", nullable = false)
    @Value(value = "0")
    private double averageRating;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Product.ProductTypeEnum productType;

    @OneToMany(mappedBy = "product")
    private List<UserProductAssociation> userProductAssociations;

    public enum ProductTypeEnum {
        BOOK, AUDIOBOOK, EBOOK
    }
}
