package baykov.daniel.springbootbookstoreapp.entity;

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
@Table(name = "narrator")
public class Narrator extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @Column(nullable = false)
    private String biography;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "city_id")
    private City city;

    private LocalDate birthDate;

    private Boolean isAlive;

    private LocalDate deathDate;

    public Narrator(Narrator narrator, Country country, City city) {
        this.setFirstName(narrator.getFirstName());
        this.setLastName(narrator.getLastName());
        this.setBiography(narrator.getBiography());
        this.setCountry(country);
        this.setCity(city);
        this.setBirthDate(narrator.getBirthDate());
        this.setIsAlive(narrator.getIsAlive());
        this.setDeathDate(narrator.getDeathDate());
    }

    public void update(Narrator narrator, Country country, City city) {
        if (narrator.getFirstName() != null)  this.setFirstName(narrator.getFirstName());
        if (narrator.getLastName() != null) this.setLastName(narrator.getLastName());
        if (narrator.getBiography() != null) this.setBiography(narrator.getBiography());
        if (country != null) this.setCountry(country);
        if (city != null) this.setCity(city);
        if (narrator.getBirthDate() != null) this.setBirthDate(narrator.getBirthDate());
        if (narrator.getIsAlive() != null) this.setIsAlive(narrator.getIsAlive());
        if (narrator.getDeathDate() != null) this.setDeathDate(narrator.getDeathDate());
    }
}
