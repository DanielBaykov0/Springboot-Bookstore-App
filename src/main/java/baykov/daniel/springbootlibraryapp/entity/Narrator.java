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
        this.firstName = narrator.getFirstName();
        this.lastName = narrator.getLastName();
        this.biography = narrator.getBiography();
        this.country = country;
        this.city = city;
        this.birthDate = narrator.getBirthDate();
        this.isAlive = narrator.getIsAlive();
        this.deathDate = narrator.getDeathDate();
    }

    public void update(Narrator narrator) {
        this.firstName = narrator.getFirstName();
        this.lastName = narrator.getLastName();
        this.biography = narrator.getBiography();
        this.country = narrator.getCountry();
        this.city = narrator.getCity();
        this.birthDate = narrator.getBirthDate();
        this.isAlive = narrator.getIsAlive();
        this.deathDate = narrator.getDeathDate();
    }
}
