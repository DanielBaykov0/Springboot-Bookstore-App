package baykov.daniel.springbootlibraryapp.repository;

import baykov.daniel.springbootlibraryapp.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
