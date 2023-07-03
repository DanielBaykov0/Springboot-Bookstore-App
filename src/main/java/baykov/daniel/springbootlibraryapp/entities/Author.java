package baykov.daniel.springbootlibraryapp.entities;

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
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authorFirstName;
    private String authorLastName;
    private String authorCountryBorn;
    private LocalDate authorBirthDate;
    private boolean isAuthorAlive;
    private LocalDate authorDeathDate;
}
