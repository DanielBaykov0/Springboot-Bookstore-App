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
@Table(name = "country")
public class Country extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    public void update(Country country) {
        this.name = country.getName();
    }
}
