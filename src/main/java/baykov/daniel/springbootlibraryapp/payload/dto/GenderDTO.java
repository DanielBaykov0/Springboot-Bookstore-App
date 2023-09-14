package baykov.daniel.springbootlibraryapp.payload.dto;

import baykov.daniel.springbootlibraryapp.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderDTO {

    MALE(User.GenderEnum.MALE.getDisplayName()),
    FEMALE(User.GenderEnum.FEMALE.getDisplayName()),
    NON_BINARY(User.GenderEnum.NON_BINARY.getDisplayName()),
    PREFER_NOT_TO_SAY(User.GenderEnum.PREFER_NOT_TO_SAY.getDisplayName());

    private final String displayName;
}
