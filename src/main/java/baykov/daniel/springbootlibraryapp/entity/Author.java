package baykov.daniel.springbootlibraryapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "author")
public class Author extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "city_id")
    private City city;

    private LocalDate birthDate;

    private Boolean isAlive;

    private LocalDate deathDate;

    public Author(Author author, Country country, City city) {
        this.setFirstName(author.getFirstName());
        this.setLastName(author.getLastName());
        this.setCountry(country);
        this.setCity(city);
        this.setBirthDate(author.getBirthDate());
        this.setIsAlive(author.getIsAlive());
        this.setDeathDate(author.getDeathDate());
    }

    public void update(Author author, Country country, City city) {
        if (author.getFirstName() != null) this.setFirstName(author.getFirstName());
        if (author.getLastName() != null) this.setLastName(author.getLastName());
        if (country != null) this.setCountry(country);
        if (city != null) this.setCity(city);
        if (author.getBirthDate() != null) this.setBirthDate(author.getBirthDate());
        if (author.getIsAlive() != null) this.setIsAlive(author.getIsAlive());
        if (author.getDeathDate() != null) this.setDeathDate(author.getDeathDate());
    }
}
