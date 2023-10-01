package baykov.daniel.springbootlibraryapp.repository;

import baykov.daniel.springbootlibraryapp.entity.Order;
import baykov.daniel.springbootlibraryapp.entity.User;
import baykov.daniel.springbootlibraryapp.payload.dto.response.OrderHistoryResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Boolean existsByUserAndProductsId(User user, Long productId);

    @Query("SELECT NEW baykov.daniel.springbootlibraryapp.payload.dto.OrderHistoryDTO(" +
            "o.id, o.comment, o.contactPhoneNumber, o.createdOn, " +
            "o.deliveredOn, o.price, o.status) " +
            "FROM Order o " +
            "JOIN o.user u " +
            "WHERE u.userProfile.id = :userProfileId")
    List<OrderHistoryResponseDTO> findOrdersByUserId(Long userProfileId);
}
