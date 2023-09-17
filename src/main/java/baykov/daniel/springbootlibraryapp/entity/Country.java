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
@Table(name = "country")
public class Country extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    public void update(Country country) {
        this.name = country.getName();
    }
}
