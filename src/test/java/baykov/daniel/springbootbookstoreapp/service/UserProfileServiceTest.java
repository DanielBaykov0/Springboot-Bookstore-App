package baykov.daniel.springbootbookstoreapp.service;

import baykov.daniel.springbootbookstoreapp.entity.User;
import baykov.daniel.springbootbookstoreapp.entity.UserProductAssociation;
import baykov.daniel.springbootbookstoreapp.entity.UserProfile;
import baykov.daniel.springbootbookstoreapp.exception.LibraryHTTPException;
import baykov.daniel.springbootbookstoreapp.exception.ResourceNotFoundException;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.UserProfileEmailRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.UserProfileRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.OrderHistoryResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.ProductProfileResponseDTO;
import baykov.daniel.springbootbookstoreapp.repository.OrderRepository;
import baykov.daniel.springbootbookstoreapp.repository.UserProductAssociationRepository;
import baykov.daniel.springbootbookstoreapp.repository.UserProfileRepository;
import baykov.daniel.springbootbookstoreapp.repository.UserRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static baykov.daniel.springbootbookstoreapp.constant.AppConstants.MESSAGE;
import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.EMAIL_NOT_CONFIRMED;
import static baykov.daniel.springbootbookstoreapp.constant.Messages.USER_MFA_UPDATED_SUCCESSFULLY;
import static baykov.daniel.springbootbookstoreapp.constant.Messages.USER_UPDATED_SUCCESSFULLY;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    private UserProfileService userProfileService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserProductAssociationRepository userProductAssociationRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Authentication authentication;

    @Captor
    private ArgumentCaptor<UserProfile> userProfileArgumentCaptor;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> emailArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @BeforeEach
    void setUp() {
        userProfileService = new UserProfileService(
                userRepository,
                userProfileRepository,
                userProductAssociationRepository,
                authenticationService,
                orderRepository
        );
    }

    @Test
    void testUpdateUserProfile_Success() {
        UserProfileRequestDTO userProfileRequestDTO = new UserProfileRequestDTO();

        UserProfile returnUserProfile = new UserProfile();
        User returnUser = new User();
        returnUser.setIsEmailVerified(true);
        when(userProfileRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(returnUserProfile));
        when(userRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(returnUser));

        UserProfile userProfile = new UserProfile();
        when(userProfileRepository.save(userProfileArgumentCaptor.capture())).thenReturn(userProfile);

        User user = new User();
        when(userRepository.save(userArgumentCaptor.capture())).thenReturn(user);

        Map<String, String> response = userProfileService.updateUserProfile(userProfileRequestDTO, authentication);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response, Map.of(MESSAGE, USER_UPDATED_SUCCESSFULLY));

        UserProfile capturedUserProfile = userProfileArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedUserProfile);
        Assertions.assertEquals(capturedUserProfile.getAddress(), userProfileRequestDTO.getAddress());
        Assertions.assertEquals(capturedUserProfile.getCity(), userProfileRequestDTO.getCity());
        Assertions.assertEquals(capturedUserProfile.getCountry(), userProfileRequestDTO.getCountry());
        Assertions.assertEquals(capturedUserProfile.getFirstName(), userProfileRequestDTO.getFirstName());
        Assertions.assertEquals(capturedUserProfile.getLastName(), userProfileRequestDTO.getLastName());
        Assertions.assertEquals(capturedUserProfile.getPhoneNumber(), userProfileRequestDTO.getPhoneNumber());
        Assertions.assertEquals(capturedUserProfile.getBirthDate(), userProfileRequestDTO.getBirthDate());

        User capturedUser = userArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedUser);
        Assertions.assertEquals(capturedUser.getAddress(), userProfileRequestDTO.getAddress());
        Assertions.assertEquals(capturedUser.getCity(), userProfileRequestDTO.getCity());
        Assertions.assertEquals(capturedUser.getCountry(), userProfileRequestDTO.getCountry());
        Assertions.assertEquals(capturedUser.getFirstName(), userProfileRequestDTO.getFirstName());
        Assertions.assertEquals(capturedUser.getLastName(), userProfileRequestDTO.getLastName());
        Assertions.assertEquals(capturedUser.getPhoneNumber(), userProfileRequestDTO.getPhoneNumber());
        Assertions.assertEquals(capturedUser.getBirthDate(), userProfileRequestDTO.getBirthDate());
    }

    @Test
    void testUpdateUserProfile_UserProfileException() {
        UserProfileRequestDTO userProfileRequestDTO = new UserProfileRequestDTO();

        when(userProfileRepository.findByEmailIgnoreCase(emailArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> userProfileService.updateUserProfile(userProfileRequestDTO, authentication)
        );

        Assertions.assertEquals("User Profile not found with email : 'null'", exception.getMessage());

        String capturedEmail = emailArgumentCaptor.getValue();
        Assertions.assertNull(capturedEmail);
    }

    @Test
    void testUpdateUserProfile_UserException() {
        UserProfileRequestDTO userProfileRequestDTO = new UserProfileRequestDTO();

        UserProfile returnUserProfile = new UserProfile();
        when(userProfileRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(returnUserProfile));
        when(userRepository.findByEmailIgnoreCase(emailArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> userProfileService.updateUserProfile(userProfileRequestDTO, authentication)
        );

        Assertions.assertEquals("User not found with email : 'null'", exception.getMessage());

        String capturedEmail = emailArgumentCaptor.getValue();
        Assertions.assertNull(capturedEmail);
    }

    @Test
    void testUpdateUserProfile_UserEmailNotVerifiedException() {
        UserProfileRequestDTO userProfileRequestDTO = new UserProfileRequestDTO();

        UserProfile returnUserProfile = new UserProfile();
        User returnUser = new User();
        returnUser.setIsEmailVerified(false);
        when(userProfileRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(returnUserProfile));
        when(userRepository.findByEmailIgnoreCase(emailArgumentCaptor.capture())).thenReturn(Optional.of(returnUser));

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> userProfileService.updateUserProfile(userProfileRequestDTO, authentication)
        );

        Assertions.assertEquals(EMAIL_NOT_CONFIRMED, exception.getMessage());
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void testUpdateUserProfileEmail_Success() {
        UserProfileEmailRequestDTO userProfileEmailRequestDTO = new UserProfileEmailRequestDTO();

        UserProfile returnUserProfile = new UserProfile();
        User returnUser = new User();
        returnUser.setIsEmailVerified(true);
        when(userProfileRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(returnUserProfile));
        when(userRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(returnUser));

        UserProfile userProfile = new UserProfile();
        when(userProfileRepository.save(userProfileArgumentCaptor.capture())).thenReturn(userProfile);

        User user = new User();
        when(userRepository.save(userArgumentCaptor.capture())).thenReturn(user);

        Map<String, String> response = userProfileService.updateUserProfileEmail(userProfileEmailRequestDTO, authentication);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response, Map.of(MESSAGE, USER_UPDATED_SUCCESSFULLY));

        UserProfile capturedUserProfile = userProfileArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedUserProfile);
        Assertions.assertEquals(capturedUserProfile.getEmail(), userProfileEmailRequestDTO.getEmail());

        User capturedUser = userArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedUser);
        Assertions.assertEquals(capturedUser.getEmail(), userProfileEmailRequestDTO.getEmail());
    }

    @Test
    void testUpdateUserProfileEmail_UserProfileException() {
        UserProfileEmailRequestDTO userProfileEmailRequestDTO = new UserProfileEmailRequestDTO();

        when(userProfileRepository.findByEmailIgnoreCase(emailArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> userProfileService.updateUserProfileEmail(userProfileEmailRequestDTO, authentication)
        );

        Assertions.assertEquals("User Profile not found with email : 'null'", exception.getMessage());

        String capturedEmail = emailArgumentCaptor.getValue();
        Assertions.assertNull(capturedEmail);
    }

    @Test
    void testUpdateUserProfileEmail_UserException() {
        UserProfileEmailRequestDTO userProfileEmailRequestDTO = new UserProfileEmailRequestDTO();

        UserProfile returnUserProfile = new UserProfile();
        when(userProfileRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(returnUserProfile));
        when(userRepository.findByEmailIgnoreCase(emailArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> userProfileService.updateUserProfileEmail(userProfileEmailRequestDTO, authentication)
        );

        Assertions.assertEquals("User not found with email : 'null'", exception.getMessage());

        String capturedEmail = emailArgumentCaptor.getValue();
        Assertions.assertNull(capturedEmail);
    }

    @Test
    void testUpdateUserProfileEmail_UserEmailNotVerifiedException() {
        UserProfileEmailRequestDTO userProfileEmailRequestDTO = new UserProfileEmailRequestDTO();

        UserProfile returnUserProfile = new UserProfile();
        User returnUser = new User();
        returnUser.setIsEmailVerified(false);
        when(userProfileRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(returnUserProfile));
        when(userRepository.findByEmailIgnoreCase(emailArgumentCaptor.capture())).thenReturn(Optional.of(returnUser));

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> userProfileService.updateUserProfileEmail(userProfileEmailRequestDTO, authentication)
        );

        Assertions.assertEquals(EMAIL_NOT_CONFIRMED, exception.getMessage());
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void testUpdateUserMfa_Success() {
        UserProfile returnUserProfile = new UserProfile();
        returnUserProfile.setMfaEnabled(false);
        User returnUser = new User();
        returnUser.setIsEmailVerified(true);
        returnUser.setMfaEnabled(false);
        when(userProfileRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(returnUserProfile));
        when(userRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(returnUser));

        UserProfile userProfile = new UserProfile();
        userProfile.setMfaEnabled(true);
        when(userProfileRepository.save(userProfileArgumentCaptor.capture())).thenReturn(userProfile);

        User user = new User();
        user.setMfaEnabled(true);
        when(userRepository.save(userArgumentCaptor.capture())).thenReturn(user);

        Map<String, String> response = userProfileService.updateUserMfa(authentication);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response, Map.of(MESSAGE, USER_MFA_UPDATED_SUCCESSFULLY));

        UserProfile capturedUserProfile = userProfileArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedUserProfile);
        Assertions.assertEquals(capturedUserProfile.getMfaEnabled(), userProfile.getMfaEnabled());

        User capturedUser = userArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedUser);
        Assertions.assertEquals(capturedUser.getMfaEnabled(), user.getMfaEnabled());
    }

    @Test
    void testUpdateUserMfa_UserProfileException() {
        when(userProfileRepository.findByEmailIgnoreCase(emailArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> userProfileService.updateUserMfa(authentication)
        );

        Assertions.assertEquals("User Profile not found with email : 'null'", exception.getMessage());

        String capturedEmail = emailArgumentCaptor.getValue();
        Assertions.assertNull(capturedEmail);
    }

    @Test
    void testUpdateUserMfa_UserException() {
        UserProfile returnUserProfile = new UserProfile();
        when(userProfileRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(returnUserProfile));
        when(userRepository.findByEmailIgnoreCase(emailArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> userProfileService.updateUserMfa(authentication)
        );

        Assertions.assertEquals("User not found with email : 'null'", exception.getMessage());

        String capturedEmail = emailArgumentCaptor.getValue();
        Assertions.assertNull(capturedEmail);
    }

    @Test
    void testUpdateUserMfa_UserEmailNotVerifiedException() {
        UserProfile returnUserProfile = new UserProfile();
        User returnUser = new User();
        returnUser.setIsEmailVerified(false);
        when(userProfileRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(returnUserProfile));
        when(userRepository.findByEmailIgnoreCase(emailArgumentCaptor.capture())).thenReturn(Optional.of(returnUser));

        LibraryHTTPException exception = Assertions.assertThrows(
                LibraryHTTPException.class,
                () -> userProfileService.updateUserMfa(authentication)
        );

        Assertions.assertEquals(EMAIL_NOT_CONFIRMED, exception.getMessage());
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void testGetProducts_Success() {
        UserProfile returnUserProfile = new UserProfile();
        returnUserProfile.setUserProductAssociations(List.of());
        when(userProfileRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(returnUserProfile));

        List<UserProductAssociation> associations = new ArrayList<>();
        when(userProductAssociationRepository.findByUserProfile(userProfileArgumentCaptor.capture())).thenReturn(associations);

        List<ProductProfileResponseDTO> productProfileResponseDTOS = userProfileService.getProducts(authentication);
        Assertions.assertNotNull(productProfileResponseDTOS);

        UserProfile capturedUserProfile = userProfileArgumentCaptor.getValue();
        int capturedUserProfileSize = capturedUserProfile.getUserProductAssociations().stream()
                .map(UserProductAssociation::getProduct)
                .toList().size();
        Assertions.assertNotNull(capturedUserProfile);
        Assertions.assertEquals(capturedUserProfileSize, productProfileResponseDTOS.size());
    }

    @Test
    void testGetProducts_UserProfileException() {
        when(userProfileRepository.findByEmailIgnoreCase(emailArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> userProfileService.getProducts(authentication)
        );

        Assertions.assertEquals("User Profile not found with email : 'null'", exception.getMessage());

        String capturedEmail = emailArgumentCaptor.getValue();
        Assertions.assertNull(capturedEmail);
    }

    @Test
    void testGetOrdersHistory_Succes() {
        UserProfile returnUserProfile = new UserProfile();
        returnUserProfile.setId(1L);
        when(userProfileRepository.findByEmailIgnoreCase(authentication.getName())).thenReturn(Optional.of(returnUserProfile));

        List<OrderHistoryResponseDTO> returnOrderHistoryResponseDTOS = new ArrayList<>();
        when(orderRepository.findOrdersByUserId(longArgumentCaptor.capture())).thenReturn(returnOrderHistoryResponseDTOS);

        List<OrderHistoryResponseDTO> orderHistoryResponseDTOS = userProfileService.getOrdersHistory(authentication);
        Assertions.assertNotNull(orderHistoryResponseDTOS);

        Long capturedUserProfileId = longArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedUserProfileId);
        Assertions.assertEquals(capturedUserProfileId, returnUserProfile.getId());
    }

    @Test
    void testGetOrdersHistory_UserProfileException() {
        when(userProfileRepository.findByEmailIgnoreCase(emailArgumentCaptor.capture())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> userProfileService.getOrdersHistory(authentication)
        );

        Assertions.assertEquals("User Profile not found with email : 'null'", exception.getMessage());

        String capturedEmail = emailArgumentCaptor.getValue();
        Assertions.assertNull(capturedEmail);
    }
}