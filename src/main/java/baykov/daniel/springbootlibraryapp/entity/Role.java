package baykov.daniel.springbootlibraryapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role extends BaseEntity {

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    public enum RoleEnum {
        ADMIN, LIBRARIAN, USER
    }
}
