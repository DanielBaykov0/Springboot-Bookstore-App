package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.Book;
import baykov.daniel.springbootbookstoreapp.entity.CommentReview;
import baykov.daniel.springbootbookstoreapp.entity.Product;
import baykov.daniel.springbootbookstoreapp.entity.User;
import baykov.daniel.springbootbookstoreapp.exception.LibraryHTTPException;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.CommentReviewRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.CommentReviewResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.repository.CommentReviewRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.USER_NOT_VERIFIED_REVIEW;
import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.USER_NO_REVIEW;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentReviewServiceTest {

    private CommentReviewService commentReviewService;

    @Mock
    private CommentReviewRepository commentReviewRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ServiceUtil serviceUtil;

    @Mock
    private Authentication authentication;

    @Captor
    private ArgumentCaptor<CommentReview> commentReviewArgumentCaptor;

    @Captor
    private ArgumentCaptor<Product> productArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> emailArgumentCaptor;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @BeforeEach
    void setUp() {
        commentReviewService = new CommentReviewService(
                commentReviewRepository, productRepository, userRepository, serviceUtil);
    }

    @Test
    void testPostReview_ReturnCommentReviewResponseDTO() {
        CommentReviewRequestDTO commentReviewRequestDTO = new CommentReviewRequestDTO();
        commentReviewRequestDTO.setProductId(1L);
        commentReviewRequestDTO.setProductType(Product.ProductTypeEnum.BOOK);

        Book book = new Book();
        book.setId(1L);
        Double avgRating = 1.0;

        User user = new User();
        user.setIsEmailVerified(true);
        when(productRepository.findById(commentReviewRequestDTO.getProductId())).thenReturn(Optional.of(book));
        when(userRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(user));

        CommentReview commentReview = new CommentReview();
        commentReview.setPostedAt(LocalDateTime.now());
        commentReview.setUpdatedAt(LocalDateTime.now());
        commentReview.setProduct(book);

        when(commentReviewRepository.save(commentReviewArgumentCaptor.capture())).thenReturn(commentReview);
        when(commentReviewRepository.findAverageRatingByProductId(longArgumentCaptor.capture())).thenReturn(avgRating);
        when(productRepository.save(productArgumentCaptor.capture())).thenReturn(book);

        CommentReviewResponseDTO commentReviewResponseDTO = commentReviewService.postReview(commentReviewRequestDTO, authentication);
        Assertions.assertNotNull(commentReviewResponseDTO);

        CommentReview capturedCommentReview = commentReviewArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedCommentReview);
        Assertions.assertEquals(capturedCommentReview.getUser(), user);
        Assertions.assertEquals(capturedCommentReview.getProduct(), book);

        Product capturedProduct = productArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedProduct);
        Assertions.assertEquals(capturedProduct.getAverageRating(), avgRating);
    }

    @Test
    void testPostReview_ReturnProductException() {
        CommentReviewRequestDTO commentReviewRequestDTO = new CommentReviewRequestDTO();

        when(productRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> commentReviewService.postReview(commentReviewRequestDTO, authentication)
        );

        Assertions.assertEquals("Product not found with ID : 'null'", exception.getMessage());

        Long capturedProductId = longArgumentCaptor.getValue();
        Assertions.assertNull(capturedProductId);
    }

    @Test
    void testPostReview_ReturnUserException() {
        CommentReviewRequestDTO commentReviewRequestDTO = new CommentReviewRequestDTO();
        commentReviewRequestDTO.setProductId(1L);
        commentReviewRequestDTO.setProductType(Product.ProductTypeEnum.BOOK);

        Book book = new Book();

        User user = new User();
        user.setIsEmailVerified(true);
        when(productRepository.findById(commentReviewRequestDTO.getProductId())).thenReturn(Optional.of(book));
        when(userRepository.findByEmailIgnoreCase(emailArgumentCaptor.capture())).thenReturn(Optional.empty());


        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> commentReviewService.postReview(commentReviewRequestDTO, authentication)
        );

        Assertions.assertEquals("User not found with email : 'null'", exception.getMessage());

        String capturedUserEmail = emailArgumentCaptor.getValue();
        Assertions.assertNull(capturedUserEmail);
    }

    @Test
    void testPostReview_ReturnUserEmailNotVerifiedException() {
        CommentReviewRequestDTO commentReviewRequestDTO = new CommentReviewRequestDTO();
        commentReviewRequestDTO.setProductId(1L);
        commentReviewRequestDTO.setProductType(Product.ProductTypeEnum.BOOK);

        Book book = new Book();

        User user = new User();
        user.setIsEmailVerified(false);
        when(productRepository.findById(commentReviewRequestDTO.getProductId())).thenReturn(Optional.of(book));
        when(userRepository.findByEmailIgnoreCase(emailArgumentCaptor.capture())).thenReturn(Optional.of(user));


        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> commentReviewService.postReview(commentReviewRequestDTO, authentication)
        );

        Assertions.assertEquals(USER_NOT_VERIFIED_REVIEW, exception.getMessage());
        Assertions.assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    void testGetProductReviewsById_ReturnCommentReviewResponseDTO() {
        Long productId = 1L;
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        String sortDir = "ASC";

        Product product = new Product();
        product.setId(1L);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Page<CommentReview> mockPage = new PageImpl<>(List.of());
        List<CommentReviewResponseDTO> content = new ArrayList<>();
        GenericResponse<CommentReviewResponseDTO> mockResponse = new GenericResponse<>();
        when(commentReviewRepository.findAllByProductId(eq(product.getId()), pageableArgumentCaptor.capture())).thenReturn(mockPage);
        when(serviceUtil.createGenericResponse(mockPage, content)).thenReturn(mockResponse);

        GenericResponse<CommentReviewResponseDTO> retrievedCommentReviewResponse = commentReviewService.getProductReviewsById(productId, pageNo, pageSize, sortBy, sortDir);
        Assertions.assertNotNull(retrievedCommentReviewResponse);

        Pageable pageable = pageableArgumentCaptor.getValue();
        Assertions.assertNotNull(pageable);
        Assertions.assertEquals(pageNo, pageable.getPageNumber());
        Assertions.assertEquals(pageSize, pageable.getPageSize());
        Assertions.assertEquals(sortBy, pageable.getSort().get().findFirst().map(Sort.Order::getProperty).orElse(null));
        Assertions.assertEquals(sortDir, pageable.getSort().get().findFirst().map(order -> order.getDirection().toString()).orElse(null));
    }

    @Test
    void testGetProductReviewsById_ReturnProductException() {
        Long productId = 1L;
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "id";
        String sortDir = "ASC";

        when(productRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> commentReviewService.getProductReviewsById(productId, pageNo, pageSize, sortBy, sortDir)
        );

        Assertions.assertEquals("Product not found with ID : '1'", exception.getMessage());

        Long capturedProductId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedProductId);
        Assertions.assertEquals(capturedProductId, productId);
    }

    @Test
    void testGetFirstRatingForProductByUser_ReturnInteger() {
        Long productId = 1L;
        String userEmail = "email@email.com";

        Product product = new Product();
        product.setId(1L);
        User user = new User();
        user.setId(1L);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(userRepository.findByEmailIgnoreCase(userEmail)).thenReturn(Optional.of(user));

        List<CommentReview> returnCommentReview = new ArrayList<>();
        returnCommentReview.add(new CommentReview());
        returnCommentReview.get(0).setRating(1);
        when(commentReviewRepository.existsByUserIdAndProductId(user.getId(), product.getId())).thenReturn(true);
        when(commentReviewRepository.findAllByUserIdAndProductIdOrderByUpdatedAtDesc(user.getId(), product.getId())).thenReturn(returnCommentReview);

        Integer rating = commentReviewService.getFirstRatingForProductByUser(productId, userEmail);
        Assertions.assertNotNull(rating);

        Assertions.assertEquals(rating, returnCommentReview.get(0).getRating());
    }

    @Test
    void testGetFirstRatingForProductByUser_ReturnProductException() {
        Long productId = 1L;
        String userEmail = "email@email.com";

        when(productRepository.findById(longArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> commentReviewService.getFirstRatingForProductByUser(productId, userEmail)
        );

        Assertions.assertEquals("Product not found with ID : '1'", exception.getMessage());

        Long capturedProductId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedProductId);
        Assertions.assertEquals(capturedProductId, productId);
    }

    @Test
    void testGetFirstRatingForProductByUser_ReturnUserException() {
        Long productId = 1L;
        String userEmail = "email@email.com";

        Product product = new Product();
        product.setId(1L);
        User user = new User();
        user.setId(1L);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(userRepository.findByEmailIgnoreCase(emailArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> commentReviewService.getFirstRatingForProductByUser(productId, userEmail)
        );

        Assertions.assertEquals("User not found with email : 'email@email.com'", exception.getMessage());

        String capturedUserEmailId = emailArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedUserEmailId);
        Assertions.assertEquals(capturedUserEmailId, userEmail);
    }

    @Test
    void testGetFirstRatingForProductByUser_ReturnUserNoReviewsException() {
        Long productId = 1L;
        String userEmail = "email@email.com";

        Product product = new Product();
        product.setId(1L);
        User user = new User();
        user.setId(1L);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(userRepository.findByEmailIgnoreCase(userEmail)).thenReturn(Optional.of(user));

        CommentReview returnCommentReview = new CommentReview();
        returnCommentReview.setRating(1);
        when(commentReviewRepository.existsByUserIdAndProductId(user.getId(), product.getId())).thenReturn(false);

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> commentReviewService.getFirstRatingForProductByUser(productId, userEmail)
        );

        Assertions.assertEquals(USER_NO_REVIEW, exception.getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}