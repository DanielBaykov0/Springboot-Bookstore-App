package baykov.daniel.springbootlibraryapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
        this.firstName = author.getFirstName();
        this.lastName = author.getLastName();
        this.country = country;
        this.city = city;
        this.birthDate = author.getBirthDate();
        this.isAlive = author.getIsAlive();
        this.deathDate = author.getDeathDate();
    }

    public void update(Author author) {
        this.firstName = author.getFirstName();
        this.lastName = author.getLastName();
        this.country = author.getCountry();
        this.city = author.getCity();
        this.birthDate = author.getBirthDate();
        this.isAlive = author.getIsAlive();
        this.deathDate = author.getDeathDate();
    }
}
