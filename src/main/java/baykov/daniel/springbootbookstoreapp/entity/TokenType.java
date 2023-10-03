package baykov.daniel.springbootbookstoreapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "token_type")
public class TokenType extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private TokenTypeEnum name;

    public enum TokenTypeEnum {
        CONFIRMATION,
        VERIFICATION,
        RESET
    }
}
