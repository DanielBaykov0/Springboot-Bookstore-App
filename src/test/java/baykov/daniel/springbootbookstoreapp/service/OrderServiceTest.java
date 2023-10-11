package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.Cart;
import baykov.daniel.springbootbookstoreapp.entity.Order;
import baykov.daniel.springbootbookstoreapp.entity.User;
import baykov.daniel.springbootbookstoreapp.entity.UserProfile;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.OrderRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.OrderResponseDTO;
import baykov.daniel.springbootbookstoreapp.repository.*;
import baykov.daniel.springbootbookstoreapp.service.util.ServiceUtil;
import com.stripe.exception.StripeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserProductAssociationRepository userProductAssociationRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ServiceUtil serviceUtil;

    @Mock
    private StripeService stripeService;

    @Mock
    private Authentication authentication;

    @Captor
    private ArgumentCaptor<Order> orderArgumentCaptor;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(
                userRepository,
                userProfileRepository,
                userProductAssociationRepository,
                orderRepository,
                cartRepository,
                serviceUtil,
                stripeService
        );
    }

    @Test
    void testPlaceOrder_Success() throws StripeException {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        User user = new User();
        UserProfile userProfile = new UserProfile();
        Cart newCart = new Cart();
        newCart.setProducts(Map.of());

        user.setCart(newCart);

        when(userRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(user));
        when(userProfileRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(userProfile));

        Order returnOrder = new Order();
        returnOrder.setProducts(List.of());
        when(orderRepository.save(orderArgumentCaptor.capture())).thenReturn(returnOrder);

        OrderResponseDTO createdOrder = orderService.placeOrder(orderRequestDTO, authentication);
        Assertions.assertNotNull(createdOrder);

        Order order = orderArgumentCaptor.getValue();
        Assertions.assertNotNull(order);
        Assertions.assertEquals(order.getComment(), orderRequestDTO.getComment());
        Assertions.assertEquals(order.getPaymentMethod(), orderRequestDTO.getPaymentMethod());
    }
}