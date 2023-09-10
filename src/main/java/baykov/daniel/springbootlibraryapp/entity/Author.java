package baykov.daniel.springbootlibraryapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    private String countryBorn;
    private LocalDate birthDate;
    private Boolean isAlive;
    private LocalDate deathDate;

    public void update(Author author) {
        this.firstName = author.getFirstName();
        this.lastName = author.getLastName();
        this.countryBorn = author.getCountryBorn();
        this.birthDate = author.getBirthDate();
        this.isAlive = author.getIsAlive();
        this.deathDate = author.getDeathDate();
    }
}
