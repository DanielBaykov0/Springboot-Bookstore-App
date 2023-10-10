package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.CommentReview;
import baykov.daniel.springbootbookstoreapp.entity.Product;
import baykov.daniel.springbootbookstoreapp.entity.User;
import baykov.daniel.springbootbookstoreapp.exception.LibraryHTTPException;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.CommentReviewRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.CommentReviewResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.mapper.CommentReviewMapper;
import baykov.daniel.springbootbookstoreapp.payload.response.GenericResponse;
import baykov.daniel.springbootbookstoreapp.repository.CommentReviewRepository;
import baykov.daniel.springbootbookstoreapp.repository.ProductRepository;
import baykov.daniel.springbootbookstoreapp.repository.UserRepository;
import baykov.daniel.springbootbookstoreapp.service.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.*;
import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.USER_NOT_VERIFIED_REVIEW;
import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.USER_NO_REVIEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentReviewService {

    private final CommentReviewRepository commentReviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ServiceUtil serviceUtil;

    @Transactional
    public CommentReviewResponseDTO postReview(CommentReviewRequestDTO commentReviewRequestDTO, Authentication authentication) {
        log.info("Posting a review...");
        Product product = productRepository.findById(commentReviewRequestDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT, ID, commentReviewRequestDTO.getProductId()));
        User user = userRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(USER, EMAIL, authentication.getName()));

        CommentReview commentReview = CommentReviewMapper.INSTANCE.dtoToEntity(commentReviewRequestDTO);

        if (!user.getIsEmailVerified()) {
            log.warn("User is not verified: {}", user.getEmail());
            throw new LibraryHTTPException(HttpStatus.FORBIDDEN, USER_NOT_VERIFIED_REVIEW);
        }

        Product.ProductTypeEnum productType = commentReviewRequestDTO.getProductType();
        if (!Arrays.asList(Product.ProductTypeEnum.values()).contains(productType)) {
            throw new ResourceNotFoundException(PRODUCT_TYPE, TYPE, productType.name());
        }

        commentReview.setUser(user);
        commentReview.setProduct(product);
        commentReview.setComment(commentReviewRequestDTO.getComment());
        commentReview.setRating(commentReviewRequestDTO.getRating());
        commentReviewRepository.save(commentReview);
        product.setAverageRating(commentReviewRepository.findAverageRatingByProductId(product.getId()));
        productRepository.save(product);

        log.info("Product review posted successfully!");
        return CommentReviewMapper.INSTANCE.entityToDTO(commentReview);
    }

    public GenericResponse<CommentReviewResponseDTO> getProductReviewsById(Long productId, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("Retrieving product reviews by Product ID: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT, ID, productId));

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<CommentReview> commentReviews = commentReviewRepository.findAllByProductId(product.getId(), pageable);
        List<CommentReviewResponseDTO> content = CommentReviewMapper.INSTANCE.entityToDTO(commentReviews.getContent());
        log.info("Retrieved {} product reviews.", commentReviews.getContent().size());
        return serviceUtil.createGenericResponse(commentReviews, content);
    }

    public Integer getFirstRatingForProductByUser(Long productId, String userEmail) {
        log.info("Getting first rating for Product ID: {} by user: {}", productId, userEmail);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT, ID, productId));
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException(USER, EMAIL, userEmail));

        if (commentReviewRepository.existsByUserIdAndProductId(user.getId(), product.getId())) {
            List<CommentReview> reviews = commentReviewRepository.findAllByUserIdAndProductIdOrderByUpdatedAtDesc(user.getId(), product.getId());
            CommentReview foundCommentReview = reviews.get(0);
            log.info("Found existing review with rating: {}", foundCommentReview.getRating());
            return foundCommentReview.getRating();
        }

        throw new LibraryHTTPException(HttpStatus.NOT_FOUND, USER_NO_REVIEW);
    }
}
