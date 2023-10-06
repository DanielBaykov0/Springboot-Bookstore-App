package baykov.daniel.springbootbookstoreapp.repository;

import baykov.daniel.springbootbookstoreapp.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
