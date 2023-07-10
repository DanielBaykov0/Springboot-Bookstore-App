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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
    private String name;
    private int userAge;
    private LocalDate dateOfBirth;
    private String userGender;
    private String userAddress;
    private String userCity;
    private String userCounty;

    @Column(nullable = false)
    private String userEmail;
}
