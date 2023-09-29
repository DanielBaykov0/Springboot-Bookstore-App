package baykov.daniel.springbootlibraryapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profile")
public class UserProfile extends BaseEntity {

    private String profilePictureUrl;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Boolean mfaEnabled;

    @OneToMany(mappedBy = "userProfile")
    private List<UserProductAssociation> userProductAssociations;

    public UserProfile(UserProfile userProfile) {
        this.setFirstName(userProfile.getFirstName());
        this.setLastName(userProfile.getLastName());
        this.setBirthDate(userProfile.getBirthDate());
        this.setPhoneNumber(userProfile.getPhoneNumber());
        this.setAddress(userProfile.getAddress());
        this.setCountry(userProfile.getCountry());
        this.setCity(userProfile.getCity());
        this.setMfaEnabled(false);
    }

    public void update(UserProfile userProfile) {
        if (userProfile.getProfilePictureUrl() != null) this.setProfilePictureUrl(userProfile.getProfilePictureUrl());
        if (userProfile.getFirstName() != null) this.setFirstName(userProfile.getFirstName());
        if (userProfile.getLastName() != null) this.setLastName(userProfile.getLastName());
        if (userProfile.getBirthDate() != null) this.setBirthDate(userProfile.getBirthDate());
        if (userProfile.getPhoneNumber() != null) this.setPhoneNumber(userProfile.getPhoneNumber());
        if (userProfile.getAddress() != null) this.setAddress(userProfile.getAddress());
        if (userProfile.getCountry() != null) this.setCountry(userProfile.getCountry());
        if (userProfile.getCity() != null) this.setCity(userProfile.getCity());
    }
}
