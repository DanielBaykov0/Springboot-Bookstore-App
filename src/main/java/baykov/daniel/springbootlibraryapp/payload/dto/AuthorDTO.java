package baykov.daniel.springbootlibraryapp.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {

    private Long id;
    private String authorFirstName;
    private String authorLastName;
    private String authorCountryBorn;
    private LocalDate authorBirthDate;
    private boolean isAuthorAlive;
    private LocalDate authorDeathDate;
}
