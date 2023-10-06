package baykov.daniel.springbootbookstoreapp.payload.dto;

import baykov.daniel.springbootbookstoreapp.validator.address.ValidAddress;
import baykov.daniel.springbootbookstoreapp.validator.email.ValidEmail;
import baykov.daniel.springbootbookstoreapp.validator.name.ValidName;
import baykov.daniel.springbootbookstoreapp.validator.password.PasswordValueMatches;
import baykov.daniel.springbootbookstoreapp.validator.password.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@PasswordValueMatches.List({
        @PasswordValueMatches(
                field = "password",
                fieldMatch = "matchingPassword",
                message = "Those passwords didn't match. Please try again."
        )
})
public class RegisterDTO {

    @Schema(description = "First name", example = "John")
    @ValidName(message = "First name should not be null or empty and should have at least 1 English character.")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    @ValidName(message = "Last name should not be null or empty and should have at least 1 English character.")
    private String lastName;

    @Schema(description = "Email", example = "john.doe@example.com")
    @NotEmpty(message = "Email should not be null or empty")
    @ValidEmail
    private String email;

    @Schema(description = "Password", example = "P@ssw0rd123")
    @NotEmpty(message = "Password should not be null or empty")
    @ValidPassword
    private String password;

    @Schema(description = "Matching password", example = "P@ssw0rd123")
    @NotEmpty(message = "Matching Password should not be null or empty")
    @ValidPassword
    private String matchingPassword;

    @Schema(description = "Birthday", example = "2000-01-01")
    @NotNull(message = "Birthday should not be empty.")
    @PastOrPresent
    private LocalDate birthDate;

    @Schema(description = "Gender (MALE, FEMALE, NON_BINARY, PREFER_NOT_TO_SAY)", example = "MALE")
    private GenderDTO gender;

    @Schema(description = "Phone number", example = "0887080808")
    @NotEmpty(message = "Phone number should not be null or empty.")
    @Size(min = 10, max = 10, message = "Phone number should have exactly 10 characters.")
    private String phoneNumber;

    @Schema(description = "Address", example = "1234 Elm St")
    @ValidAddress(message = "Please enter a valid address.")
    private String address;

    @Schema(description = "Country", example = "United States")
    @ValidName(message = "Country should not be null or empty and should have at least 1 English character.")
    private String country;

    @Schema(description = "City", example = "New York")
    @ValidName(message = "City should not be null or empty and should have at least 1 English character.")
    private String city;

    @Schema(description = "GDPR acceptance", example = "true")
    @NotNull(message = "GDPR should not be null.")
    @AssertTrue(message = "GDPR Should be accepted!")
    private Boolean euGDPR;
}
