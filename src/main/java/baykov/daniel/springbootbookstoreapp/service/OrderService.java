package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.*;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.OrderRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.OrderResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.mapper.OrderMapper;
import baykov.daniel.springbootbookstoreapp.repository.*;
import baykov.daniel.springbootbookstoreapp.service.util.ServiceUtil;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserProductAssociationRepository userProductAssociationRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ServiceUtil serviceUtil;
    private final StripeService stripeService;

    @Transactional
    public OrderResponseDTO placeOrder(OrderRequestDTO orderRequest, Authentication authentication) throws StripeException {
        log.info("Order placement initiated by user: {}", authentication.getName());
        User user = userRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(USER, EMAIL, authentication.getName()));
        UserProfile userProfile = userProfileRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(USER_PROFILE, EMAIL, authentication.getName()));

        List<CartItem> cartItems = new ArrayList<>(user.getCart().getProducts().values());

        List<Product> products = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            products.add(cartItem.getProduct());
        }

        Order order = OrderMapper.INSTANCE.dtoToEntity(orderRequest, user.getCart());
        order.setPrice(OrderMapper.INSTANCE.calculatePrice(user.getCart()));
        order.setPaymentIntentId(stripeService.createPaymentIntent(order.getPrice()));
        order.setAddress(userProfile.getAddress());
        order.setContactPhoneNumber(userProfile.getPhoneNumber());
        order.setUser(user);
        order.setComment(orderRequest.getComment());
        order.setProducts(products);
        orderRepository.save(order);

        for (Product product : products) {
            UserProductAssociation userProductAssociation = new UserProductAssociation();
            userProductAssociation.setUserProfile(userProfile);
            userProductAssociation.setProduct(product);
            userProductAssociationRepository.save(userProductAssociation);
        }

        cartRepository.save(serviceUtil.clearUserCart(user));
        log.info("Order successfully placed for user: {}", authentication.getName());
        return OrderMapper.INSTANCE.entityToDTO(order);
    }
}
