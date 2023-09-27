package baykov.daniel.springbootlibraryapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Boolean isActive;

    private Boolean isEmailVerified;

    private Boolean mfaEnabled;

    private String secret;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @OneToOne
    private Cart cart;

    public User(User user, String password, Set<Role> roles) {
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setEmail(user.getEmail());
        this.setPassword(password);
        this.setBirthDate(user.getBirthDate());
        this.setGender(user.getGender());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setAddress(user.getAddress());
        this.setCountry(user.getCountry());
        this.setCity(user.getCity());
        this.setIsEmailVerified(false);
        this.setIsActive(true);
        this.setMfaEnabled(false);
        this.setRoles(roles);
    }

    public void update(User user) {
        if (user.getFirstName() != null) this.setFirstName(user.getFirstName());
        if (user.getLastName() != null) this.setLastName(user.getLastName());
        if (user.getEmail() != null) this.setEmail(user.getEmail());
        if (user.getBirthDate() != null) this.setBirthDate(user.getBirthDate());
        if (user.getPhoneNumber() != null) this.setPhoneNumber(user.getPhoneNumber());
        if (user.getAddress() != null) this.setAddress(user.getAddress());
        if (user.getCountry() != null) this.setCountry(user.getCountry());
        if (user.getCity() != null) this.setCity(user.getCity());
    }

    @Getter
    @AllArgsConstructor
    public enum GenderEnum {
        MALE("Male"),
        FEMALE("Female"),
        NON_BINARY("Non-Binary"),
        PREFER_NOT_TO_SAY("Prefer Not to Say");

        private final String displayName;
    }
}
