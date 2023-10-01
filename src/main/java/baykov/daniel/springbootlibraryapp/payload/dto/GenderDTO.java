package baykov.daniel.springbootlibraryapp.payload.dto;

import baykov.daniel.springbootlibraryapp.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum GenderDTO {

    MALE(User.GenderEnum.MALE.getDisplayName()),
    FEMALE(User.GenderEnum.FEMALE.getDisplayName()),
    NON_BINARY(User.GenderEnum.NON_BINARY.getDisplayName()),
    PREFER_NOT_TO_SAY(User.GenderEnum.PREFER_NOT_TO_SAY.getDisplayName());

    @Schema(description = "Display name for the gender", example = "MALE")
    private final String displayName;
}
