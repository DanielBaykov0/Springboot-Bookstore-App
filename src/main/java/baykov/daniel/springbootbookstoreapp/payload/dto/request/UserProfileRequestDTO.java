package baykov.daniel.springbootbookstoreapp.payload.dto.request;

import baykov.daniel.springbootbookstoreapp.validator.address.ValidAddress;
import baykov.daniel.springbootbookstoreapp.validator.name.ValidName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class UserProfileRequestDTO {

    @Schema(description = "URL of the user's profile picture", example = "https://example.com/profile.jpg")
    @Pattern(regexp = "^(http|https)://[a-zA-Z0-9.-]+(:[0-9]+)?(/[a-zA-Z0-9./-]*)?$")
    private String profilePictureUrl;

    @Schema(description = "First name of the user", example = "John")
    @ValidName(message = "First name should not be null or empty and should have at least 1 English character.")
    private String firstName;

    @Schema(description = "Last name of the user", example = "Doe")
    @ValidName(message = "Last name should not be null or empty and should have at least 1 English character.")
    private String lastName;

    @Schema(description = "Birth date of the user (YYYY-MM-DD)", example = "1990-01-01")
    @NotNull(message = "Birthday should not be empty.")
    @PastOrPresent
    private LocalDate birthDate;

    @Schema(description = "Phone number of the user", example = "0887080808")
    @NotEmpty(message = "Phone number should not be null or empty.")
    @Size(min = 10, max = 10, message = "Phone number should have exactly 10 characters.")
    private String phoneNumber;

    @Schema(description = "Address of the user", example = "1234 Elm St, Apt 101")
    @ValidAddress(message = "Please enter a valid address.")
    private String address;

    @Schema(description = "Country of the user", example = "United States")
    @ValidName(message = "Country should not be null or empty and should have at least 1 English character.")
    private String country;

    @Schema(description = "City of the user", example = "New York")
    @ValidName(message = "City should not be null or empty and should have at least 1 English character.")
    private String city;
}
