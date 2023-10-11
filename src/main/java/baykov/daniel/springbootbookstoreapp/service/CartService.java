package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.*;
import baykov.daniel.springbootbookstoreapp.exception.LibraryHTTPException;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.CartRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.CartResponseDTO;
import baykov.daniel.springbootbookstoreapp.repository.ProductRepository;
import baykov.daniel.springbootbookstoreapp.repository.UserRepository;
import baykov.daniel.springbootbookstoreapp.service.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.*;
import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ServiceUtil serviceUtil;

    @Transactional
    public CartResponseDTO addToCart(Authentication authentication, CartRequestDTO cartRequestDTO) {
        log.info("Received request to add to cart. User: {}, Product ID: {}, Product Type: {}",
                authentication.getName(), cartRequestDTO.getProductId(), cartRequestDTO.getProductType());

        User user = userRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(USER, EMAIL, authentication.getName()));
        Product product = productRepository.findByIdAndProductType(cartRequestDTO.getProductId(), cartRequestDTO.getProductType())
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_TYPE, TYPE, cartRequestDTO.getProductType().name()));

        if (!user.getIsEmailVerified()) {
            log.error("Add to cart failed for user with unverified email: {}", authentication.getName());
            throw new LibraryHTTPException(HttpStatus.FORBIDDEN, EMAIL_NOT_VERIFIED);
        }

        if (user.getCart().getProducts().containsKey(product) && !product.getProductType().equals(Product.ProductTypeEnum.BOOK)) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, CANT_BUY_MORE_THAN_ONE);
        }

        switch (product.getProductType()) {
            case BOOK:
                serviceUtil.handleAddBookToCart(user, (Book) product, cartRequestDTO.getQuantity());
                break;
            case EBOOK:
                serviceUtil.handleAddEbookToCart(user, (Ebook) product, cartRequestDTO.getQuantity());
                break;
            case AUDIOBOOK:
                serviceUtil.handleAddAudiobookToCart(user, (Audiobook) product, cartRequestDTO.getQuantity());
                break;
            default:
                throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, UNKNOWN_PRODUCT_TYPE);
        }

        userRepository.save(user);
        log.info("Product added to cart successfully. User: {}, Product ID: {}, Product Type: {}",
                authentication.getName(), cartRequestDTO.getProductId(), cartRequestDTO.getProductType());
        return serviceUtil.createCartResponse(user);
    }

    @Transactional
    public CartResponseDTO removeFromCart(Authentication authentication, CartRequestDTO cartRequestDTO) {
        log.info("Received request to remove from cart. User: {}, Product ID: {}, Product Type: {}",
                authentication.getName(), cartRequestDTO.getProductId(), cartRequestDTO.getProductType());

        User user = userRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(USER, EMAIL, authentication.getName()));
        Product product = productRepository.findByIdAndProductType(cartRequestDTO.getProductId(), cartRequestDTO.getProductType())
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_TYPE, TYPE, cartRequestDTO.getProductType().name()));

        switch (product.getProductType()) {
            case BOOK:
                serviceUtil.handleRemoveBookFromCart(user, (Book) product, cartRequestDTO.getQuantity());
                break;
            case EBOOK:
                serviceUtil.handleRemoveEbookFromCart(user, (Ebook) product, cartRequestDTO.getQuantity());
                break;
            case AUDIOBOOK:
                serviceUtil.handleRemoveAudiobookFromCart(user, (Audiobook) product, cartRequestDTO.getQuantity());
                break;
            default:
                throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, UNKNOWN_PRODUCT_TYPE);
        }

        userRepository.save(user);
        log.info("Product removed from cart successfully. User: {}, Product ID: {}, Product Type: {}",
                authentication.getName(), cartRequestDTO.getProductId(), cartRequestDTO.getProductType());
        return serviceUtil.createCartResponse(user);
    }

    public CartResponseDTO getUserCart(Authentication authentication) {
        log.info("Received request to get user cart. User: {}", authentication.getName());
        User user = userRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(USER, EMAIL, authentication.getName()));

        log.info("Retrieved user cart successfully. User: {}", authentication.getName());
        return serviceUtil.createCartResponse(user);
    }
}
