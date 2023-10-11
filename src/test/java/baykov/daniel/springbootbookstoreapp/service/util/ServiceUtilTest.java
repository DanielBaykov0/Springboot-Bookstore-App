package baykov.daniel.springbootbookstoreapp.service.util;

import baykov.daniel.springbootbookstoreapp.entity.*;
import baykov.daniel.springbootbookstoreapp.exception.LibraryHTTPException;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceUtilTest {

    private ServiceUtil serviceUtil;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private Audiobook audiobook1;

    @Mock
    private Audiobook audiobook2;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<Cart> cartArgumentCaptor;

    @BeforeEach
    void setUp() {
        serviceUtil = new ServiceUtil(
                roleRepository, tokenRepository, productRepository, orderRepository, cartRepository);
    }

    @Test
    void testSetRoles_Success() {
        Role userRole = new Role();
        userRole.setName(Role.RoleEnum.ROLE_USER);

        when(roleRepository.findByName(Role.RoleEnum.ROLE_USER)).thenReturn(Optional.of(userRole));

        Set<Role> roles = serviceUtil.setRoles();

        Assertions.assertEquals(1, roles.size());
        Assertions.assertEquals(userRole, roles.iterator().next());
    }

    @Test
    void testValidateIDs_Success() {
        List<Long> requestedIDs = Arrays.asList(1L, 2L);
        List<BaseEntity> foundEntities = Arrays.asList(audiobook1, audiobook2);

        when(audiobook1.getId()).thenReturn(1L);
        when(audiobook2.getId()).thenReturn(2L);

        serviceUtil.validateIDs(requestedIDs, foundEntities, "audiobook");
    }

    @Test
    void testValidateIDs_ReturnInvalidIDException() {
        List<Long> requestedIDs = Arrays.asList(1L, 2L, 3L);
        List<BaseEntity> foundEntities = Arrays.asList(audiobook1, audiobook2);

        when(audiobook1.getId()).thenReturn(1L);
        when(audiobook2.getId()).thenReturn(2L);


        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> serviceUtil.validateIDs(requestedIDs, foundEntities, "Audiobook")
        );

        Assertions.assertEquals("Audiobook not found with ID : '3'", exception.getMessage());
    }

    @Test
    void testCheckTokenExpired_Success() {
        String token = "notExpiredToken";

        Token notExpiredToken = new Token();
        notExpiredToken.setExpiresAt(LocalDateTime.now());

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(notExpiredToken));

        serviceUtil.checkTokenExpired(token);
    }

    @Test
    void testCheckTokenExpired_ReturnTokenInvalidException() {
        String token = "notValidToken";

        when(tokenRepository.findByToken(stringArgumentCaptor.capture())).thenReturn(Optional.empty());

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> serviceUtil.checkTokenExpired(token)
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        Assertions.assertEquals(TOKEN_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testCheckTokenExpired_ReturnPreviousTokenNotExpiredException() {
        String token = "notExpiredToken";

        Token expiredToken = new Token();
        expiredToken.setExpiresAt(LocalDateTime.now().plusMinutes(1));

        when(tokenRepository.findByToken(stringArgumentCaptor.capture())).thenReturn(Optional.of(expiredToken));

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> serviceUtil.checkTokenExpired(token)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(PREVIOUS_TOKEN_NOT_EXPIRED, exception.getMessage());
    }

    @Test
    void testCheckTokenValid_ReturnTokenInvalidException() {
        String token = "notExpiredToken";

        Token notExpiredToken = new Token();
        notExpiredToken.setExpiresAt(LocalDateTime.now());

        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(notExpiredToken));

        serviceUtil.checkTokenValid(token);
    }

    @Test
    void testCheckTokenValid_Success() {
        String token = "notValidToken";

        when(tokenRepository.findByToken(stringArgumentCaptor.capture())).thenReturn(Optional.empty());

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> serviceUtil.checkTokenValid(token)
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        Assertions.assertEquals(TOKEN_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testCheckTokenValid_ReturnPreviousTokenExpiredException() {
        String token = "еxpiredToken";

        Token expiredToken = new Token();
        expiredToken.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        when(tokenRepository.findByToken(stringArgumentCaptor.capture())).thenReturn(Optional.of(expiredToken));

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> serviceUtil.checkTokenValid(token)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(PREVIOUS_TOKEN_EXPIRED, exception.getMessage());
    }

    @Test
    void testHandleAddBookToCart_Success() {
        User user = new User();
        user.setCart(new Cart());
        Book book = new Book();
        book.setId(4L);
        book.setNumberOfAvailableCopies(5);
        book.setNumberOfTotalCopies(5);
        book.setPrice(BigDecimal.TEN);
        book.setProductType(Product.ProductTypeEnum.BOOK);

        Integer quantity = 1;

        when(productRepository.save(book)).thenReturn(book);

        serviceUtil.handleAddBookToCart(user, book, quantity);

        Assertions.assertEquals(4, book.getNumberOfTotalCopies());
        Assertions.assertEquals(4, book.getNumberOfAvailableCopies());
    }

    @Test
    void testHandleAddBookToCart_ReturnQuantityExceedsProductsException() {
        User user = new User();
        user.setCart(new Cart());
        Book book = new Book();
        book.setId(4L);
        book.setNumberOfAvailableCopies(5);
        book.setNumberOfTotalCopies(5);
        book.setPrice(BigDecimal.TEN);
        book.setProductType(Product.ProductTypeEnum.BOOK);

        Integer quantity = 6;

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> serviceUtil.handleAddBookToCart(user, book, quantity)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(QUANTITY_EXCEEDS_PRODUCTS_AVAILABLE, exception.getMessage());
    }

    @Test
    void testHandleAddBookToCart_ReturnNegativeQuantityException() {
        User user = new User();
        user.setCart(new Cart());
        Book book = new Book();
        book.setId(4L);
        book.setNumberOfAvailableCopies(5);
        book.setNumberOfTotalCopies(5);
        book.setPrice(BigDecimal.TEN);
        book.setProductType(Product.ProductTypeEnum.BOOK);

        Integer quantity = -1;

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> serviceUtil.handleAddBookToCart(user, book, quantity)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(NEGATIVE_QUANTITY, exception.getMessage());
    }

    @Test
    void testHandleRemoveBookFromCart_Success() {
        User user = new User();
        user.setCart(new Cart());
        Book book = new Book();
        book.setId(4L);
        book.setNumberOfAvailableCopies(5);
        book.setNumberOfTotalCopies(5);
        book.setPrice(BigDecimal.TEN);
        book.setProductType(Product.ProductTypeEnum.BOOK);
        user.getCart().addProduct(book, 1);

        Integer quantity = 1;

        when(productRepository.save(book)).thenReturn(book);

        serviceUtil.handleRemoveBookFromCart(user, book, quantity);

        Assertions.assertEquals(6, book.getNumberOfTotalCopies());
        Assertions.assertEquals(6, book.getNumberOfAvailableCopies());
    }

    @Test
    void testHandleRemoveBookFromCart_ReturnQuantityExceedsProductsException() {
        User user = new User();
        user.setCart(new Cart());
        Book book = new Book();
        book.setId(4L);
        book.setNumberOfAvailableCopies(5);
        book.setNumberOfTotalCopies(5);
        book.setPrice(BigDecimal.TEN);
        book.setProductType(Product.ProductTypeEnum.BOOK);
        user.getCart().addProduct(book, 1);

        Integer quantity = 5;

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> serviceUtil.handleRemoveBookFromCart(user, book, quantity)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(QUANTITY_EXCEEDS_ADDED_PRODUCTS, exception.getMessage());
    }

    @Test
    void testHandleRemoveBookFromCart_ReturnNegativeQuantityException() {
        User user = new User();
        user.setCart(new Cart());
        Book book = new Book();
        book.setId(4L);
        book.setNumberOfAvailableCopies(5);
        book.setNumberOfTotalCopies(5);
        book.setPrice(BigDecimal.TEN);
        book.setProductType(Product.ProductTypeEnum.BOOK);
        user.getCart().addProduct(book, 1);

        Integer quantity = -1;

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> serviceUtil.handleRemoveBookFromCart(user, book, quantity)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(NEGATIVE_QUANTITY, exception.getMessage());
    }

    @Test
    void testHandleAddEbookToCart_Success() {
        User user = new User();
        user.setCart(new Cart());
        Ebook ebook = new Ebook();
        ebook.setId(4L);
        ebook.setNumberOfAvailableCopies(1);
        ebook.setPrice(BigDecimal.TEN);
        ebook.setProductType(Product.ProductTypeEnum.EBOOK);

        Integer quantity = 1;

        serviceUtil.handleAddEbookToCart(user, ebook, quantity);

        Assertions.assertTrue(user.getCart().getProducts().containsKey(ebook));
        Assertions.assertEquals(1, user.getCart().getProducts().size());
    }

    @Test
    void testHandleAddEbookToCart_ReturnEbookBoughtAlreadyException() {
        User user = new User();
        user.setCart(new Cart());
        Ebook ebook = new Ebook();
        ebook.setId(4L);
        ebook.setNumberOfAvailableCopies(1);
        ebook.setPrice(BigDecimal.TEN);
        ebook.setProductType(Product.ProductTypeEnum.EBOOK);

        Integer quantity = 1;

        when(orderRepository.existsByUserAndProductsId(user, ebook.getId())).thenReturn(true);

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> serviceUtil.handleAddEbookToCart(user, ebook, quantity)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(EBOOK_BOUGHT_ALREADY, exception.getMessage());
    }

    @Test
    void testHandleAddEbookToCart_ReturnEbookBuyOnlyOneException() {
        User user = new User();
        user.setCart(new Cart());
        Ebook ebook = new Ebook();
        ebook.setId(4L);
        ebook.setNumberOfAvailableCopies(1);
        ebook.setPrice(BigDecimal.TEN);
        ebook.setProductType(Product.ProductTypeEnum.EBOOK);

        Integer quantity = 2;

        when(orderRepository.existsByUserAndProductsId(user, ebook.getId())).thenReturn(false);

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> serviceUtil.handleAddEbookToCart(user, ebook, quantity)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(CAN_BUY_ONLY_ONE, exception.getMessage());
    }

    @Test
    void testHandleAddEbookToCart_ReturnEbookNegativeQuantityException() {
        User user = new User();
        user.setCart(new Cart());
        Ebook ebook = new Ebook();
        ebook.setId(4L);
        ebook.setNumberOfAvailableCopies(1);
        ebook.setPrice(BigDecimal.TEN);
        ebook.setProductType(Product.ProductTypeEnum.EBOOK);

        Integer quantity = -1;

        when(orderRepository.existsByUserAndProductsId(user, ebook.getId())).thenReturn(false);

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> serviceUtil.handleAddEbookToCart(user, ebook, quantity)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(NEGATIVE_QUANTITY, exception.getMessage());
    }

    @Test
    void testHandleRemoveEbookFromCart_Success() {
        User user = new User();
        user.setCart(new Cart());
        Ebook ebook = new Ebook();
        ebook.setId(4L);
        ebook.setNumberOfAvailableCopies(1);
        ebook.setPrice(BigDecimal.TEN);
        ebook.setProductType(Product.ProductTypeEnum.BOOK);
        user.getCart().addProduct(ebook, 1);

        Integer quantity = 1;

        serviceUtil.handleRemoveEbookFromCart(user, ebook, quantity);

        Assertions.assertFalse(user.getCart().getProducts().containsKey(ebook));
        Assertions.assertEquals(0, user.getCart().getProducts().size());
    }

    @Test
    void testHandleRemoveEbookFromCart_ReturnQuantityExceedsProductsException() {
        User user = new User();
        user.setCart(new Cart());
        Ebook ebook = new Ebook();
        ebook.setId(4L);
        ebook.setNumberOfAvailableCopies(1);
        ebook.setPrice(BigDecimal.TEN);
        ebook.setProductType(Product.ProductTypeEnum.BOOK);
        user.getCart().addProduct(ebook, 1);

        Integer quantity = 2;

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> serviceUtil.handleRemoveEbookFromCart(user, ebook, quantity)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(QUANTITY_EXCEEDS_ADDED_PRODUCTS, exception.getMessage());
    }

    @Test
    void testHandleRemoveEbookFromCart_ReturnNegativeQuantityException() {
        User user = new User();
        user.setCart(new Cart());
        Ebook ebook = new Ebook();
        ebook.setId(4L);
        ebook.setNumberOfAvailableCopies(1);
        ebook.setPrice(BigDecimal.TEN);
        ebook.setProductType(Product.ProductTypeEnum.BOOK);
        user.getCart().addProduct(ebook, 1);

        Integer quantity = -1;

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> serviceUtil.handleRemoveEbookFromCart(user, ebook, quantity)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(NEGATIVE_QUANTITY, exception.getMessage());
    }

    @Test
    void testHandleAddAudiobookToCart_Success() {
        User user = new User();
        user.setCart(new Cart());
        Audiobook audiobook = new Audiobook();
        audiobook.setId(4L);
        audiobook.setNumberOfAvailableCopies(1);
        audiobook.setPrice(BigDecimal.TEN);
        audiobook.setProductType(Product.ProductTypeEnum.AUDIOBOOK);

        Integer quantity = 1;

        serviceUtil.handleAddAudiobookToCart(user, audiobook, quantity);

        Assertions.assertTrue(user.getCart().getProducts().containsKey(audiobook));
        Assertions.assertEquals(1, user.getCart().getProducts().size());
    }

    @Test
    void testHandleAddAudiobookToCart_ReturnAudiobookBoughtAlreadyException() {
        User user = new User();
        user.setCart(new Cart());
        Audiobook audiobook = new Audiobook();
        audiobook.setId(4L);
        audiobook.setNumberOfAvailableCopies(1);
        audiobook.setPrice(BigDecimal.TEN);
        audiobook.setProductType(Product.ProductTypeEnum.AUDIOBOOK);

        Integer quantity = 1;

        when(orderRepository.existsByUserAndProductsId(user, audiobook.getId())).thenReturn(true);

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> serviceUtil.handleAddAudiobookToCart(user, audiobook, quantity)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(AUDIOBOOK_BOUGHT_ALREADY, exception.getMessage());
    }

    @Test
    void testHandleAddAudiobookToCart_ReturnAudiobookBuyOnlyOneException() {
        User user = new User();
        user.setCart(new Cart());
        Audiobook audiobook = new Audiobook();
        audiobook.setId(4L);
        audiobook.setNumberOfAvailableCopies(1);
        audiobook.setPrice(BigDecimal.TEN);
        audiobook.setProductType(Product.ProductTypeEnum.AUDIOBOOK);

        Integer quantity = 2;

        when(orderRepository.existsByUserAndProductsId(user, audiobook.getId())).thenReturn(false);

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> serviceUtil.handleAddAudiobookToCart(user, audiobook, quantity)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(CAN_BUY_ONLY_ONE, exception.getMessage());
    }

    @Test
    void testHandleAddAudiobookToCart_ReturnAudiobookNegativeQuantityException() {
        User user = new User();
        user.setCart(new Cart());
        Audiobook audiobook = new Audiobook();
        audiobook.setId(4L);
        audiobook.setNumberOfAvailableCopies(1);
        audiobook.setPrice(BigDecimal.TEN);
        audiobook.setProductType(Product.ProductTypeEnum.AUDIOBOOK);

        Integer quantity = -1;

        when(orderRepository.existsByUserAndProductsId(user, audiobook.getId())).thenReturn(false);

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> serviceUtil.handleAddAudiobookToCart(user, audiobook, quantity)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(NEGATIVE_QUANTITY, exception.getMessage());
    }

    @Test
    void testHandleRemoveAudiobookFromCart_Success() {
        User user = new User();
        user.setCart(new Cart());
        Audiobook audiobook = new Audiobook();
        audiobook.setId(4L);
        audiobook.setNumberOfAvailableCopies(1);
        audiobook.setPrice(BigDecimal.TEN);
        audiobook.setProductType(Product.ProductTypeEnum.AUDIOBOOK);
        user.getCart().addProduct(audiobook, 1);

        Integer quantity = 1;

        serviceUtil.handleRemoveAudiobookFromCart(user, audiobook, quantity);

        Assertions.assertFalse(user.getCart().getProducts().containsKey(audiobook));
        Assertions.assertEquals(0, user.getCart().getProducts().size());
    }

    @Test
    void testHandleRemoveAudiobookFromCart_ReturnQuantityExceedsProductsException() {
        User user = new User();
        user.setCart(new Cart());
        Audiobook audiobook = new Audiobook();
        audiobook.setId(4L);
        audiobook.setNumberOfAvailableCopies(1);
        audiobook.setPrice(BigDecimal.TEN);
        audiobook.setProductType(Product.ProductTypeEnum.AUDIOBOOK);
        user.getCart().addProduct(audiobook, 1);

        Integer quantity = 2;

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> serviceUtil.handleRemoveAudiobookFromCart(user, audiobook, quantity)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(QUANTITY_EXCEEDS_ADDED_PRODUCTS, exception.getMessage());
    }

    @Test
    void testHandleRemoveAudiobookFromCart_ReturnNegativeQuantityException() {
        User user = new User();
        user.setCart(new Cart());
        Audiobook audiobook = new Audiobook();
        audiobook.setId(4L);
        audiobook.setNumberOfAvailableCopies(1);
        audiobook.setPrice(BigDecimal.TEN);
        audiobook.setProductType(Product.ProductTypeEnum.AUDIOBOOK);
        user.getCart().addProduct(audiobook, 1);

        Integer quantity = -1;

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> serviceUtil.handleRemoveAudiobookFromCart(user, audiobook, quantity)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertEquals(NEGATIVE_QUANTITY, exception.getMessage());
    }

    @Test
    void getNewCart() {
        Cart savedCart = new Cart();
        savedCart.setId(1L);

        when(cartRepository.saveAndFlush(cartArgumentCaptor.capture())).thenReturn(savedCart);

        Cart newCart = serviceUtil.getNewCart();
        Assertions.assertNotNull(newCart);

        Cart capturedCart = cartArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedCart);
        Assertions.assertNull(capturedCart.getId());
    }

    @Test
    void clearUserCart() {
        User user = new User();
        Cart cart = new Cart();
        cart.setProductsCount(2L);
        cart.setProductsSum(BigDecimal.valueOf(100.0));

        user.setCart(cart);

        Cart clearedCart = serviceUtil.clearUserCart(user);

        Assertions.assertNotNull(clearedCart);
        Assertions.assertEquals(0, clearedCart.getProducts().size());
        Assertions.assertEquals(0L, clearedCart.getProductsCount());
        Assertions.assertEquals(BigDecimal.ZERO, clearedCart.getProductsSum());
    }
}