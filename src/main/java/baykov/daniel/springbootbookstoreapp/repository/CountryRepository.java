package baykov.daniel.springbootbookstoreapp.repository;

import baykov.daniel.springbootbookstoreapp.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
