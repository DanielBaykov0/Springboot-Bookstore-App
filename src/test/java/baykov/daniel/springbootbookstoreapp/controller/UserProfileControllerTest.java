package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.payload.dto.request.UserProfileEmailRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.UserProfileRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static baykov.daniel.springbootbookstoreapp.constant.Messages.USER_MFA_UPDATED_SUCCESSFULLY;
import static baykov.daniel.springbootbookstoreapp.constant.Messages.USER_UPDATED_SUCCESSFULLY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static UserProfileRequestDTO getUserProfileRequestDTO() {
        UserProfileRequestDTO userProfileRequestDTO = new UserProfileRequestDTO();
        userProfileRequestDTO.setFirstName("first name");
        userProfileRequestDTO.setLastName("last name");
        userProfileRequestDTO.setBirthDate(LocalDate.of(2000, 4, 4));
        userProfileRequestDTO.setPhoneNumber("0887080808");
        userProfileRequestDTO.setAddress("1234 Elm St, Apt 101");
        userProfileRequestDTO.setCountry("USA");
        userProfileRequestDTO.setCity("LA");
        return userProfileRequestDTO;
    }

    @Test
    @WithMockUser(username = "martin@gmail.com", roles = "USER")
    void testUpdateUserProfile_Success() throws Exception {
        UserProfileRequestDTO userProfileRequestDTO = getUserProfileRequestDTO();

        mockMvc.perform(put("/api/v1/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userProfileRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(USER_UPDATED_SUCCESSFULLY));
    }


    @Test
    @WithMockUser(username = "martin@gmail.com", roles = "USER")
    void testUpdateUserProfileEmail_Success() throws Exception {
        UserProfileEmailRequestDTO userProfileEmailRequestDTO = new UserProfileEmailRequestDTO();
        userProfileEmailRequestDTO.setEmail("example@example.com");

        mockMvc.perform(put("/api/v1/profile/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userProfileEmailRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(USER_UPDATED_SUCCESSFULLY));
    }

    @Test
    @WithMockUser(username = "martin@gmail.com", roles = "USER")
    void testUpdateUserMfaStatus_Success() throws Exception {
        mockMvc.perform(put("/api/v1/profile/mfa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(USER_MFA_UPDATED_SUCCESSFULLY));
    }

    @Test
    @WithMockUser(username = "martin@gmail.com", roles = "USER")
    void testListUserProducts_Success() throws Exception {
        mockMvc.perform(get("/api/v1/profile/my-products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").isNumber());
    }

    @Test
    @WithMockUser(username = "martin@gmail.com", roles = "USER")
    void testListUserOrdersHistory_Success() throws Exception {
        mockMvc.perform(get("/api/v1/profile/my-orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").isNumber());
    }
}