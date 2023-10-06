package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.User;
import baykov.daniel.springbootbookstoreapp.entity.UserProductAssociation;
import baykov.daniel.springbootbookstoreapp.entity.UserProfile;
import baykov.daniel.springbootbookstoreapp.exception.LibraryHTTPException;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.OrderHistoryResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.UserProfileEmailRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.UserProfileRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.ProductProfileResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.mapper.ProductMapper;
import baykov.daniel.springbootbookstoreapp.payload.mapper.UserMapper;
import baykov.daniel.springbootbookstoreapp.payload.mapper.UserProfileMapper;
import baykov.daniel.springbootbookstoreapp.repository.OrderRepository;
import baykov.daniel.springbootbookstoreapp.repository.UserProductAssociationRepository;
import baykov.daniel.springbootbookstoreapp.repository.UserProfileRepository;
import baykov.daniel.springbootbookstoreapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.*;
import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.EMAIL_NOT_CONFIRMED;
import static baykov.daniel.springbootbookstoreapp.constant.Messages.USER_MFA_UPDATED_SUCCESSFULLY;
import static baykov.daniel.springbootbookstoreapp.constant.Messages.USER_UPDATED_SUCCESSFULLY;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserProductAssociationRepository userProductAssociationRepository;
    private final AuthenticationService authenticationService;
    private final OrderRepository orderRepository;

    @Transactional
    public Map<String, String> updateUserProfile(UserProfileRequestDTO userProfileRequestDTO, Authentication authentication) {
        log.info("Update profile initiated for user: {}", authentication.getName());
        UserProfile userProfile = userProfileRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(USER_PROFILE, EMAIL, authentication.getName()));
        User user = userRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(USER, EMAIL, authentication.getName()));

        if (!user.getIsEmailVerified()) {
            log.error("Update profile failed - User email not verified: {}", authentication.getName());
            throw new LibraryHTTPException(HttpStatus.UNAUTHORIZED, EMAIL_NOT_CONFIRMED);
        }

        UserProfile updatedUserProfile = UserProfileMapper.INSTANCE.dtoToEntity(userProfileRequestDTO);
        userProfile.update(updatedUserProfile);
        userProfileRepository.save(userProfile);

        User updatedUser = UserMapper.INSTANCE.dtoToEntity(userProfileRequestDTO);
        user.update(updatedUser);
        userRepository.save(user);
        log.info("User profile updated successfully for user: {}", authentication.getName());
        return Map.of(MESSAGE, USER_UPDATED_SUCCESSFULLY);
    }

    @Transactional
    public Map<String, String> updateUserProfileEmail(UserProfileEmailRequestDTO userProfileEmailRequestDTO, Authentication authentication) {
        log.info("Updating user profile email for user: {}", authentication.getName());
        UserProfile userProfile = userProfileRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(USER_PROFILE, EMAIL, authentication.getName()));
        User user = userRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(USER, EMAIL, authentication.getName()));

        if (!user.getIsEmailVerified()) {
            log.error("Update profile email failed - User email not verified: {}", authentication.getName());
            throw new LibraryHTTPException(HttpStatus.UNAUTHORIZED, EMAIL_NOT_CONFIRMED);
        }

        userProfile.setEmail(userProfileEmailRequestDTO.getEmail());
        userProfileRepository.save(userProfile);

        user.setEmail(userProfile.getEmail());
        userRepository.save(user);

        authenticationService.logout();

        log.info("User profile email updated successfully for user: {}", authentication.getName());
        return Map.of(MESSAGE, USER_UPDATED_SUCCESSFULLY);
    }

    @Transactional
    public Map<String, String> updateUserMfa(Authentication authentication) {
        log.info("Updating multi-factor authentication (MFA) for user: {}", authentication.getName());
        UserProfile userProfile = userProfileRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(USER_PROFILE, EMAIL, authentication.getName()));
        User user = userRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(USER, EMAIL, authentication.getName()));

        if (!user.getIsEmailVerified()) {
            log.error("Update MFA failed - User email not verified: {}", authentication.getName());
            throw new LibraryHTTPException(HttpStatus.UNAUTHORIZED, EMAIL_NOT_CONFIRMED);
        }

        userProfile.setMfaEnabled(!userProfile.getMfaEnabled());
        userProfileRepository.save(userProfile);

        user.setMfaEnabled(userProfile.getMfaEnabled());
        userRepository.save(user);

        log.info("MFA status updated successfully for user: {}", authentication.getName());
        return Map.of(MESSAGE, USER_MFA_UPDATED_SUCCESSFULLY);
    }

    public List<ProductProfileResponseDTO> getProducts(Authentication authentication) {
        log.info("Retrieving products for user: {}", authentication.getName());
        UserProfile userProfile = userProfileRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(USER_PROFILE, EMAIL, authentication.getName()));

        List<UserProductAssociation> associations = userProductAssociationRepository.findByUserProfile(userProfile);
        log.debug("Found {} associations for user: {}", associations.size(), authentication.getName());

        List<ProductProfileResponseDTO> productProfileResponseDTOS = associations.stream()
                .map(association -> ProductMapper.INSTANCE.entityToDTO(association.getProduct()))
                .collect(Collectors.toList());
        log.info("Retrieved {} products for user: {}", productProfileResponseDTOS.size(), authentication.getName());
        return productProfileResponseDTOS;
    }

    public List<OrderHistoryResponseDTO> getOrdersHistory(Authentication authentication) {
        log.info("Retrieving order history for user: {}", authentication.getName());
        UserProfile userProfile = userProfileRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(USER_PROFILE, EMAIL, authentication.getName()));

        List<OrderHistoryResponseDTO> orderHistoryResponseDTOS = orderRepository.findOrdersByUserId(userProfile.getId());

        log.info("Retrieved {} order(s) for user: {}", orderHistoryResponseDTOS.size(), authentication.getName());
        return orderHistoryResponseDTOS;
    }
}
