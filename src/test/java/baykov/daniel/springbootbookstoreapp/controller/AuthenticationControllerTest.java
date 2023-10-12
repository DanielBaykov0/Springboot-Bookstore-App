package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.payload.dto.*;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.JwtRefreshRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.VerificationRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.INVALID_JWT_TOKEN;
import static baykov.daniel.springbootbookstoreapp.constant.Messages.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static RegisterDTO getRegisterDTO() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setFirstName("first name");
        registerDTO.setLastName("last name");
        registerDTO.setEmail("email@abv.com");
        registerDTO.setPassword("!Example123");
        registerDTO.setMatchingPassword("!Example123");
        registerDTO.setBirthDate(LocalDate.of(2000, 4, 4));
        registerDTO.setGender(GenderDTO.MALE);
        registerDTO.setPhoneNumber("0887080808");
        registerDTO.setAddress("1234 Elm St");
        registerDTO.setCountry("USA");
        registerDTO.setCity("LA");
        registerDTO.setEuGDPR(true);
        return registerDTO;
    }

    @Test
    void testRegister_Success() throws Exception {
        RegisterDTO registerDTO = getRegisterDTO();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(REGISTERED_SUCCESSFULLY));
    }

    @Test
    void testLogin_Success() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("martin@gmail.com");
        loginDTO.setPassword("!Martin123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString())
                .andExpect(jsonPath("$.secretImageUri").isString());
    }

    @Test
    @WithMockUser(username = "martin@gmail.com", roles = "USER")
    void testVerifyCode_400_NotCorrect() throws Exception {
        VerificationRequestDTO verificationRequestDTO = new VerificationRequestDTO();
        verificationRequestDTO.setCode("code");

        mockMvc.perform(post("/api/v1/auth/verify-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificationRequestDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(VERIFICATION_CODE_NOT_CORRECT));
    }

    @Test
    @WithMockUser(username = "martin@gmail.com", roles = "USER")
    void testCurrentUser_Success() throws Exception {
        String response = mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andReturn().getResponse().getContentAsString();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response, "martin@gmail.com");
    }

    @Test
    @WithMockUser(username = "martin@gmail.com", roles = "USER")
    void testLogout_Success() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(LOGOUT_SUCCESS));
    }

    @Test
    @WithMockUser(username = "martin@gmail.com", roles = "USER")
    void testRefreshToken_400_InvalidJWTToken() throws Exception {
        JwtRefreshRequestDTO jwtRefreshRequestDTO = new JwtRefreshRequestDTO();
        jwtRefreshRequestDTO.setRefreshToken("refresh token");

        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jwtRefreshRequestDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(INVALID_JWT_TOKEN));
    }

    @Test
    @WithMockUser(username = "martin@gmail.com", roles = "USER")
    void testChangePassword_500_ServerError() throws Exception {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setPassword("!Password123");
        changePasswordDTO.setMatchingPassword("!Password123");

        String token = "token";

        mockMvc.perform(patch("/api/v1/auth/change-password?token=", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordDTO)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message").value("The given id must not be null"));
    }

    @Test
    void testForgotPassword_Success() throws Exception {
        ForgotPasswordDTO forgotPasswordDTO = new ForgotPasswordDTO();
        forgotPasswordDTO.setEmail("martin@gmail.com");

        mockMvc.perform(post("/api/v1/auth/forgot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(forgotPasswordDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(EMAIL_RESET_SENT));
    }

    @Test
    void testResendForgotPassword_500_ServerError() throws Exception {
        String token = "token";

        mockMvc.perform(post("/api/v1/auth/resend-forgot?token=", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.message").value("The given id must not be null"));
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = "USER")
    void testSendEmailVerification_Success() throws Exception {
        mockMvc.perform(post("/api/v1/auth/send-email-verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SUCCESSFULLY_RESEND_VERIFICATION_EMAIL));
    }

    @Test
    void testVerifyEmail() throws Exception {
        String token = "token";

        mockMvc.perform(get("/api/v1/auth/verify-email?token=", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Token not found with value : ''"));
    }
}