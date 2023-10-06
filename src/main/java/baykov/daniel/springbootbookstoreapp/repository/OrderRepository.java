package baykov.daniel.springbootbookstoreapp.repository;

import baykov.daniel.springbootbookstoreapp.entity.Order;
import baykov.daniel.springbootbookstoreapp.entity.User;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.OrderHistoryResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.ORDER_HISTORY_DTO_PATH;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Boolean existsByUserAndProductsId(User user, Long productId);

    @Query("SELECT NEW " + ORDER_HISTORY_DTO_PATH +
            "(o.id, o.comment, o.contactPhoneNumber, o.createdOn, " +
            "o.deliveredOn, o.price, o.status) " +
            "FROM Order o " +
            "JOIN o.user u " +
            "WHERE u.userProfile.id = :userProfileId")
    List<OrderHistoryResponseDTO> findOrdersByUserId(Long userProfileId);
}
