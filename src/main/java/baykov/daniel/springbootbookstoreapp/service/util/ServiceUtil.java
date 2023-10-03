package baykov.daniel.springbootbookstoreapp.service.util;

import baykov.daniel.springbootbookstoreapp.entity.*;
import baykov.daniel.springbootbookstoreapp.exception.LibraryHTTPException;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.CartResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.ProductResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.ID;
import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceUtil {

    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public <T> GenericResponse<T> createGenericResponse(Page<?> content, List<T> mappedContent) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setContent(mappedContent);
        response.setPageNo(content.getNumber());
        response.setPageSize(content.getSize());
        response.setTotalElements(content.getTotalElements());
        response.setTotalPages(content.getTotalPages());
        response.setLast(content.isLast());
        return response;
    }

    public Set<Role> setRoles() {
        Set<Role> roles = new HashSet<>();
        Optional<Role> userRole = roleRepository.findByName(Role.RoleEnum.ROLE_USER);
        Role role = new Role();
        if (userRole.isPresent()) {
            role = userRole.get();
        }
        roles.add(role);
        return roles;
    }

    public void validateIDs(List<Long> requestedIDs, List<?> foundEntities, String entityName) {
        log.info("Validating IDs for entity: {}", entityName);
        Set<Long> foundIds = foundEntities.stream()
                .map(entity -> ((BaseEntity) entity).getId())
                .collect(Collectors.toSet());

        log.debug("Found {} existing IDs for entity: {}", foundIds.size(), entityName);

        for (Long requestedId : requestedIDs) {
            if (!foundIds.contains(requestedId)) {
                log.error("Validation failed: ID not found for entity '{}'. Requested ID: {}", entityName, requestedId);
                throw new ResourceNotFoundException(entityName, ID, requestedId);
            }
        }

        log.info("ID validation successful for entity: {}", entityName);
    }

    public void checkTokenExpired(String token) {
        log.info("Checking if token is expired.");
        Token foundToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.error("Token not found: {}", token);
                    return new LibraryHTTPException(HttpStatus.NOT_FOUND, TOKEN_NOT_FOUND);
                });

        if (foundToken.getExpiresAt().isAfter(LocalDateTime.now())) {
            log.error("Token is not expired: {}", token);
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, PREVIOUS_TOKEN_NOT_EXPIRED);
        }

        log.info("Token check for expiration completed.");
    }

    public void checkTokenValid(String token) {
        log.info("Checking if token is valid.");
        Token foundToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.error("Token not found: {}", token);
                    return new LibraryHTTPException(HttpStatus.NOT_FOUND, TOKEN_NOT_FOUND);
                });

        if (foundToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.error("Token is expired: {}", token);
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, PREVIOUS_TOKEN_EXPIRED);
        }

        log.info("Token check for validity completed.");
    }

    public void handleAddBookToCart(User user, Book book, Integer quantity) {
        log.info("Handling addition of book to cart for user: {} - Book ID: {} - Quantity: {}", user.getId(), book.getId(), quantity);
        if (quantity > book.getNumberOfAvailableCopies()) {
            log.error("Quantity exceeds available copies for book with ID: {}", book.getId());
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, QUANTITY_EXCEEDS_PRODUCTS_AVAILABLE);
        }

        if (quantity <= 0) {
            log.error("Invalid quantity ({}). Quantity should be greater than 0.", quantity);
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, NEGATIVE_QUANTITY);
        }

        user.getCart().addProduct(book, quantity);

        book.setNumberOfAvailableCopies(book.getNumberOfAvailableCopies() - quantity);
        book.setNumberOfTotalCopies(book.getNumberOfTotalCopies() - quantity);
        log.info("Added to cart book with ID {} - New available copies: {}", book.getId(), book.getNumberOfAvailableCopies());
        log.info("Added to cart book with ID {} - New total copies: {}", book.getId(), book.getNumberOfAvailableCopies());
        productRepository.save(book);
    }

    public void handleRemoveBookToCart(User user, Book book, Integer quantity) {
        log.info("Handling removal of book from cart for user: {} - Book ID: {} - Quantity: {}", user.getId(), book.getId(), quantity);

        if (quantity > user.getCart().getProductQuantity(book)) {
            log.error("Quantity exceeds added products for book with ID: {}", book.getId());
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, QUANTITY_EXCEEDS_ADDED_PRODUCTS);
        }

        if (quantity <= 0) {
            log.error("Invalid quantity ({}). Quantity should be greater than 0.", quantity);
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, NEGATIVE_QUANTITY);
        }

        user.getCart().removeProduct(book, quantity);

        book.setNumberOfAvailableCopies(book.getNumberOfAvailableCopies() + quantity);
        book.setNumberOfTotalCopies(book.getNumberOfTotalCopies() + quantity);
        log.info("Removed from cart book with ID {} - New available copies: {}", book.getId(), book.getNumberOfAvailableCopies());
        log.info("Removed from cart book with ID {} - New total copies: {}", book.getId(), book.getNumberOfAvailableCopies());
        productRepository.save(book);
    }

    public void handleAddEbookToCart(User user, Ebook ebook, Integer quantity) {
        log.info("Handling addition of ebook to cart for user: {} - Ebook ID: {} - Quantity: {}", user.getId(), ebook.getId(), quantity);

        if (orderRepository.existsByUserAndProductsId(user, ebook.getId())) {
            log.error("Ebook with ID: {} has already been bought.", ebook.getId());
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, EBOOK_BOUGHT_ALREADY);
        }

        if (quantity > ebook.getNumberOfAvailableCopies()) {
            log.error("Quantity exceeds available copies for ebook with ID: {}", ebook.getId());
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, CAN_BUY_ONLY_ONE);
        }

        if (quantity <= 0) {
            log.error("Invalid quantity ({}). Quantity should be greater than 0.", quantity);
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, NEGATIVE_QUANTITY);
        }

        if (quantity == 1) {
            user.getCart().addProduct(ebook, quantity);
            log.info("Added 1 ebook copy to the cart. Ebook ID: {}", ebook.getId());
        } else {
            log.error("Attempted to add more than 1 ebook copy with ID: {} to the cart.", ebook.getId());
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, CANT_BUY_MORE_THAN_ONE);
        }

        log.info("Added to cart ebook with ID {}", ebook.getId());
    }

    public void handleRemoveEbookToCart(User user, Ebook ebook, Integer quantity) {
        log.info("Handling removal of ebook from cart for user: {} - Ebook ID: {} - Quantity: {}", user.getId(), ebook.getId(), quantity);

        if (quantity > user.getCart().getProductQuantity(ebook)) {
            log.error("Attempted to remove more ebook copies ({} copies) than added to the cart ({} copies) for ebook with ID: {}", quantity, user.getCart().getProductQuantity(ebook), ebook.getId());
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, QUANTITY_EXCEEDS_ADDED_PRODUCTS);
        }

        if (quantity <= 0) {
            log.error("Invalid quantity ({}). Quantity should be greater than 0.", quantity);
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, NEGATIVE_QUANTITY);
        }

        log.info("Removed {} ebook copies from the cart. Ebook ID: {}", quantity, ebook.getId());
        user.getCart().removeProduct(ebook, quantity);

        productRepository.save(ebook);
        log.info("Ebook removal from cart completed for user: {} - Ebook ID: {}", user.getId(), ebook.getId());
    }

    public void handleAddAudiobookToCart(User user, Audiobook audiobook, Integer quantity) {
        log.info("Handling addition of audiobook to cart for user: {} - Audiobook ID: {} - Quantity: {}", user.getId(), audiobook.getId(), quantity);

        if (orderRepository.existsByUserAndProductsId(user, audiobook.getId())) {
            log.error("Audiobook with ID: {} has already been bought by the user.", audiobook.getId());
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, AUDIOBOOK_BOUGHT_ALREADY);
        }

        if (quantity > audiobook.getNumberOfAvailableCopies()) {
            log.error("Attempted to buy more copies ({} copies) than available ({} copies) for audiobook with ID: {}", quantity, audiobook.getNumberOfAvailableCopies(), audiobook.getId());
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, CAN_BUY_ONLY_ONE);
        }

        if (quantity <= 0) {
            log.error("Invalid quantity ({}). Quantity should be greater than 0.", quantity);
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, NEGATIVE_QUANTITY);
        }

        if (quantity == 1) {
            user.getCart().addProduct(audiobook, quantity);
        } else {
            log.error("Attempted to buy more than one copy ({} copies) at a time for audiobook with ID: {}", quantity, audiobook.getId());
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, CANT_BUY_MORE_THAN_ONE);
        }

        log.info("Added {} copy of audiobook to the cart. Audiobook ID: {}", quantity, audiobook.getId());
    }

    public void handleRemoveAudiobookToCart(User user, Audiobook audiobook, Integer quantity) {
        log.info("Handling removal of audiobook from cart for user: {} - Audiobook ID: {} - Quantity: {}", user.getId(), audiobook.getId(), quantity);

        if (quantity > user.getCart().getProductQuantity(audiobook)) {
            log.error("Attempted to remove more copies ({} copies) than added ({} copies) for audiobook with ID: {}", quantity, user.getCart().getProductQuantity(audiobook), audiobook.getId());
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, QUANTITY_EXCEEDS_ADDED_PRODUCTS);
        }

        if (quantity <= 0) {
            log.error("Invalid quantity ({}). Quantity should be greater than 0.", quantity);
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, NEGATIVE_QUANTITY);
        }

        user.getCart().removeProduct(audiobook, quantity);
        log.info("Removed {} copy of audiobook from the cart. Audiobook ID: {}", quantity, audiobook.getId());

        productRepository.save(audiobook);
        log.info("Audiobook removal from the cart completed successfully.");
    }

    public CartResponseDTO createCartResponse(User user) {
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setProducts(user.getCart().getProducts().entrySet().stream()
                .map(entry -> {
                    ProductResponseDTO productResponseDTO = new ProductResponseDTO();
                    Product product = entry.getKey();
                    CartItem cartItem = entry.getValue();
                    productResponseDTO.setProductType(product.getProductType());
                    productResponseDTO.setProductName(product.getTitle());
                    productResponseDTO.setProductPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                    productResponseDTO.setQuantity(cartItem.getQuantity());
                    return productResponseDTO;
                })
                .collect(Collectors.toList()));

        cartResponseDTO.setProductsCount(user.getCart().getProductsCount());
        cartResponseDTO.setProductsSum(user.getCart().getProductsSum());
        return cartResponseDTO;
    }

    public Cart getNewCart() {
        log.info("Creating a new cart.");
        Cart cart = new Cart();
        this.cartRepository.saveAndFlush(cart);

        log.info("New cart created with ID: {}", cart.getId());
        return cart;
    }

    public Cart clearUserCart(User user) {
        log.info("Clearing cart for user with ID: {}", user.getId());
        user.getCart().getProducts().clear();
        user.getCart().setProductsCount(0L);
        user.getCart().setProductsSum(BigDecimal.ZERO);

        log.info("Cart cleared successfully for user with ID: {}", user.getId());
        return user.getCart();
    }
}
