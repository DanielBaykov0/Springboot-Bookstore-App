package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.payload.dto.request.AuthorRequestDTO;
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

import static baykov.daniel.springbootbookstoreapp.constant.Messages.AUTHOR_DELETED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN", "LIBRARIAN"})
    void testCreateAuthor_Success() throws Exception {
        AuthorRequestDTO authorRequestDTO = new AuthorRequestDTO();
        authorRequestDTO.setFirstName("first name");
        authorRequestDTO.setLastName("last name");
        authorRequestDTO.setCountryId(1L);
        authorRequestDTO.setCityId(1L);
        authorRequestDTO.setBirthDate(LocalDate.of(2000, 4, 4));
        authorRequestDTO.setDeathDate(LocalDate.now());

        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(authorRequestDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(authorRequestDTO.getLastName()))
                .andExpect(jsonPath("$.countryId").value(authorRequestDTO.getCountryId().toString()))
                .andExpect(jsonPath("$.cityId").value(authorRequestDTO.getCityId().toString()))
                .andExpect(jsonPath("$.birthDate").value(authorRequestDTO.getBirthDate().toString()))
                .andExpect(jsonPath("$.deathDate").value(authorRequestDTO.getDeathDate().toString()));
    }

    @Test
    void testGetAuthorById_Success() throws Exception {
        Long authorId = 1L;

        mockMvc.perform(get("/api/v1/authors/{authorId}", authorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").isString())
                .andExpect(jsonPath("$.lastName").isString())
                .andExpect(jsonPath("$.birthDate").exists())
                .andExpect(jsonPath("$.firstName").isNotEmpty())
                .andExpect(jsonPath("$.lastName").isNotEmpty())
                .andExpect(jsonPath("$.birthDate").isNotEmpty());
    }

    @Test
    void testGetAllAuthors_Success() throws Exception {
        mockMvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").isNumber());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN", "LIBRARIAN"})
    void testUpdateAuthorById_Success() throws Exception {
        Long authorId = 1L;
        AuthorRequestDTO authorRequestDTO = new AuthorRequestDTO();
        authorRequestDTO.setFirstName("first name");
        authorRequestDTO.setLastName("last name");
        authorRequestDTO.setCountryId(1L);
        authorRequestDTO.setCityId(1L);
        authorRequestDTO.setBirthDate(LocalDate.of(2000, 4, 4));
        authorRequestDTO.setDeathDate(LocalDate.now());

        mockMvc.perform(put("/api/v1/authors/{authorId}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(authorRequestDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(authorRequestDTO.getLastName()))
                .andExpect(jsonPath("$.countryId").value(authorRequestDTO.getCountryId().toString()))
                .andExpect(jsonPath("$.cityId").value(authorRequestDTO.getCityId().toString()))
                .andExpect(jsonPath("$.birthDate").value(authorRequestDTO.getBirthDate().toString()))
                .andExpect(jsonPath("$.deathDate").value(authorRequestDTO.getDeathDate().toString()));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN", "LIBRARIAN"})
    void testDeleteAuthorById_Success() throws Exception {
        Long authorId = 1L;

        mockMvc.perform(delete("/api/v1/authors/{authorId}", authorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string(AUTHOR_DELETED));
    }

    @Test
    void testGetAllAuthorsByFirstNameOrLastNameOrCountryOrCity_Success() throws Exception {
        String name = "Bulgaria";

        mockMvc.perform(get("/api/v1/authors/search?name=", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").isNumber());
    }
}