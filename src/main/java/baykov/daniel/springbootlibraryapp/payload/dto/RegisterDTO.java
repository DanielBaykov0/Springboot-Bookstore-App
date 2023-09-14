package baykov.daniel.springbootlibraryapp.payload.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterDTO {

    @NotEmpty(message = "First Name should not be null or empty")
    @Size(min = 2, message = "First Name should have at least 2 characters")
    private String firstName;

    @NotEmpty(message = "Last Name should not be null or empty")
    @Size(min = 6, message = "Last Name should have at least 6 characters")
    private String lastName;

    @NotEmpty(message = "Email should not be null or empty")
    @Email
    private String email;

    @NotEmpty(message = "Password should not be null or empty")
    @Size(min = 8, max = 20, message = "Password should be between 8 and 20 characters")
    private String password;

    @NotNull(message = "Birthday should not be empty")
    @PastOrPresent
    private LocalDate birthDate;

    private GenderDTO gender;

    @NotEmpty(message = "Phone number should not be null or empty")
    @Size(min = 10, max = 10, message = "Phone number should have exactly 10 characters")
    private String phoneNumber;

    @NotEmpty(message = "Address should not be null or empty")
    @Size(min = 6, message = "Address should have at least 6 characters")
    private String address;

    @NotEmpty(message = "Country should not be null or empty")
    @Size(min = 2, message = "Country should have at least 4 characters")
    private String country;

    @NotEmpty(message = "City should not be null or empty")
    @Size(min = 4, message = "City should have at least 4 characters")
    private String city;

    @NotNull(message = "GDPR should not be null")
    @AssertTrue(message = "GDPR Should be accepted!")
    private Boolean euGDPR;
}
