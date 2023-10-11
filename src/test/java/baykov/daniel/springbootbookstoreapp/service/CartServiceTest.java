package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.*;
import baykov.daniel.springbootbookstoreapp.exception.LibraryHTTPException;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.CartRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.CartResponseDTO;
import baykov.daniel.springbootbookstoreapp.repository.ProductRepository;
import baykov.daniel.springbootbookstoreapp.repository.UserRepository;
import baykov.daniel.springbootbookstoreapp.service.util.ServiceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.Optional;

import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.CANT_BUY_MORE_THAN_ONE;
import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.EMAIL_NOT_VERIFIED;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    private CartService cartService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ServiceUtil serviceUtil;

    @Mock
    private Authentication authentication;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> emailArgumentCaptor;

    @Captor
    private ArgumentCaptor<Product.ProductTypeEnum> productTypeEnumArgumentCaptor;

    @BeforeEach
    void setUp() {
        cartService = new CartService(userRepository, productRepository, serviceUtil);
    }

    @Test
    void testAddToCart_ReturnCartResponseDTO() {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setProductId(1L);
        cartRequestDTO.setProductType(Product.ProductTypeEnum.BOOK);
        cartRequestDTO.setQuantity(2);

        User user = new User();
        user.setIsEmailVerified(true);
        user.setCart(new Cart());

        Book book = new Book();
        book.setId(1L);
        book.setProductType(Product.ProductTypeEnum.BOOK);

        when(userRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(user));
        when(productRepository.findByIdAndProductType(anyLong(), any())).thenReturn(Optional.of(book));
        when(userRepository.save(userArgumentCaptor.capture())).thenReturn(user);

        CartResponseDTO returnCartResponse = new CartResponseDTO();
        when(serviceUtil.createCartResponse(user)).thenReturn(returnCartResponse);

        CartResponseDTO cartResponseDTO = cartService.addToCart(authentication, cartRequestDTO);
        Assertions.assertNotNull(cartResponseDTO);

        User capturedUser = userArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedUser);
        Assertions.assertEquals(capturedUser.getCart().getProductsSum(), BigDecimal.ZERO);
        Assertions.assertEquals(capturedUser.getCart().getProducts().size(), 0);
        Assertions.assertEquals(capturedUser.getCart().getProductsCount(), 0);
    }

    @Test
    void testAddToCart_ReturnUserException() {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();

        when(userRepository.findByEmailIgnoreCase(emailArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> cartService.addToCart(authentication, cartRequestDTO)
        );

        Assertions.assertEquals("User not found with email : 'null'", exception.getMessage());

        String capturedEmail = emailArgumentCaptor.getValue();
        Assertions.assertNull(capturedEmail);
    }

    @Test
    void testAddToCart_ReturnProductException() {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setProductId(1L);
        cartRequestDTO.setProductType(Product.ProductTypeEnum.BOOK);

        User user = new User();

        when(userRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(user));
        when(productRepository.findByIdAndProductType(eq(cartRequestDTO.getProductId()), productTypeEnumArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> cartService.addToCart(authentication, cartRequestDTO)
        );

        Assertions.assertEquals("Product type not found with type : 'BOOK'", exception.getMessage());

        Product.ProductTypeEnum capturedProductType = productTypeEnumArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedProductType);
    }

    @Test
    void testAddToCart_ReturnUserEmailNotVerifiedException() {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setProductId(1L);
        cartRequestDTO.setProductType(Product.ProductTypeEnum.BOOK);

        User user = new User();
        user.setIsEmailVerified(false);

        Book book = new Book();

        when(userRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(user));
        when(productRepository.findByIdAndProductType(eq(cartRequestDTO.getProductId()), productTypeEnumArgumentCaptor.capture())).thenReturn(Optional.of(book));

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> cartService.addToCart(authentication, cartRequestDTO)
        );

        Assertions.assertEquals(EMAIL_NOT_VERIFIED, exception.getMessage());
        Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void testAddToCart_ReturnCantBuyMoreThanOneProductException() {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setProductId(1L);
        cartRequestDTO.setProductType(Product.ProductTypeEnum.BOOK);

        Audiobook audiobook = new Audiobook();
        audiobook.setId(1L);
        audiobook.setProductType(Product.ProductTypeEnum.AUDIOBOOK);
        audiobook.setPrice(BigDecimal.ONE);

        User user = new User();
        user.setIsEmailVerified(true);
        user.setCart(new Cart());
        user.getCart().addProduct(audiobook, 2);

        when(userRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(user));
        when(productRepository.findByIdAndProductType(eq(cartRequestDTO.getProductId()), productTypeEnumArgumentCaptor.capture())).thenReturn(Optional.of(audiobook));

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> cartService.addToCart(authentication, cartRequestDTO)
        );

        Assertions.assertEquals(CANT_BUY_MORE_THAN_ONE, exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void testRemoveFromCart_ReturnCartResponseDTO() {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setProductId(1L);
        cartRequestDTO.setProductType(Product.ProductTypeEnum.BOOK);
        cartRequestDTO.setQuantity(2);

        User user = new User();
        user.setIsEmailVerified(true);
        user.setCart(new Cart());

        Book book = new Book();
        book.setId(1L);
        book.setProductType(Product.ProductTypeEnum.BOOK);

        when(userRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(user));
        when(productRepository.findByIdAndProductType(anyLong(), any())).thenReturn(Optional.of(book));
        when(userRepository.save(userArgumentCaptor.capture())).thenReturn(user);

        CartResponseDTO returnCartResponse = new CartResponseDTO();
        when(serviceUtil.createCartResponse(user)).thenReturn(returnCartResponse);

        CartResponseDTO cartResponseDTO = cartService.removeFromCart(authentication, cartRequestDTO);
        Assertions.assertNotNull(cartResponseDTO);

        User capturedUser = userArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedUser);
        Assertions.assertEquals(capturedUser.getCart().getProductsSum(), BigDecimal.ZERO);
        Assertions.assertEquals(capturedUser.getCart().getProducts().size(), 0);
        Assertions.assertEquals(capturedUser.getCart().getProductsCount(), 0);
    }

    @Test
    void testRemoveFromCart_ReturnUserException() {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();

        when(userRepository.findByEmailIgnoreCase(emailArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> cartService.removeFromCart(authentication, cartRequestDTO)
        );

        Assertions.assertEquals("User not found with email : 'null'", exception.getMessage());

        String capturedEmail = emailArgumentCaptor.getValue();
        Assertions.assertNull(capturedEmail);
    }

    @Test
    void testRemoveFromCart_ReturnProductException() {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setProductId(1L);
        cartRequestDTO.setProductType(Product.ProductTypeEnum.BOOK);

        User user = new User();

        when(userRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(user));
        when(productRepository.findByIdAndProductType(eq(cartRequestDTO.getProductId()), productTypeEnumArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> cartService.removeFromCart(authentication, cartRequestDTO)
        );

        Assertions.assertEquals("Product type not found with type : 'BOOK'", exception.getMessage());

        Product.ProductTypeEnum capturedProductType = productTypeEnumArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedProductType);
    }

    @Test
    void testGetUserCart_ReturnCartResponseDTO() {
        User user = new User();
        user.setCart(new Cart());

        when(userRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(user));

        CartResponseDTO returnObject = new CartResponseDTO();
        when(serviceUtil.createCartResponse(userArgumentCaptor.capture())).thenReturn(returnObject);

        CartResponseDTO cartResponseDTO = cartService.getUserCart(authentication);
        Assertions.assertNotNull(cartResponseDTO);

        User capturedUser = userArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedUser);
        Assertions.assertEquals(capturedUser.getCart().getProductsSum(), BigDecimal.ZERO);
        Assertions.assertEquals(capturedUser.getCart().getProducts().size(), 0);
        Assertions.assertEquals(capturedUser.getCart().getProductsCount(), 0);
    }
}