package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.payload.dto.request.NarratorRequestDTO;
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

import static baykov.daniel.springbootbookstoreapp.constant.Messages.NARRATOR_DELETED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NarratorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN", "LIBRARIAN"})
    void testCreateNarrator_Sucess() throws Exception {
        NarratorRequestDTO narratorRequestDTO = new NarratorRequestDTO();
        narratorRequestDTO.setFirstName("first name");
        narratorRequestDTO.setLastName("last name");
        narratorRequestDTO.setCountryId(1L);
        narratorRequestDTO.setCityId(1L);
        narratorRequestDTO.setBirthDate(LocalDate.of(2000, 4, 4));
        narratorRequestDTO.setDeathDate(LocalDate.now());
        narratorRequestDTO.setBiography("Biography biography");

        mockMvc.perform(post("/api/v1/narrators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(narratorRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(narratorRequestDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(narratorRequestDTO.getLastName()))
                .andExpect(jsonPath("$.countryId").value(narratorRequestDTO.getCountryId().toString()))
                .andExpect(jsonPath("$.cityId").value(narratorRequestDTO.getCityId().toString()))
                .andExpect(jsonPath("$.birthDate").value(narratorRequestDTO.getBirthDate().toString()))
                .andExpect(jsonPath("$.deathDate").value(narratorRequestDTO.getDeathDate().toString()))
                .andExpect(jsonPath("$.biography").value(narratorRequestDTO.getBiography()));
    }

    @Test
    void testGetNarratorById_Success() throws Exception {
        Long narratorId = 1L;

        mockMvc.perform(get("/api/v1/narrators/{narratorId}", narratorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").isString())
                .andExpect(jsonPath("$.lastName").isString())
                .andExpect(jsonPath("$.birthDate").exists())
                .andExpect(jsonPath("$.firstName").isNotEmpty())
                .andExpect(jsonPath("$.lastName").isNotEmpty())
                .andExpect(jsonPath("$.birthDate").isNotEmpty())
                .andExpect(jsonPath("$.biography").isNotEmpty());
    }

    @Test
    void testGetAllNarrators_Success() throws Exception {
        mockMvc.perform(get("/api/v1/narrators"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").isNumber());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN", "LIBRARIAN"})
    void testUpdateNarratorById_Success() throws Exception {
        Long narratorId = 1L;
        NarratorRequestDTO narratorRequestDTO = new NarratorRequestDTO();
        narratorRequestDTO.setFirstName("first name");
        narratorRequestDTO.setLastName("last name");
        narratorRequestDTO.setCountryId(1L);
        narratorRequestDTO.setCityId(1L);
        narratorRequestDTO.setBirthDate(LocalDate.of(2000, 4, 4));
        narratorRequestDTO.setDeathDate(LocalDate.now());
        narratorRequestDTO.setBiography("Biography biography");

        mockMvc.perform(put("/api/v1/narrators/{narratorId}", narratorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(narratorRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(narratorRequestDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(narratorRequestDTO.getLastName()))
                .andExpect(jsonPath("$.countryId").value(narratorRequestDTO.getCountryId().toString()))
                .andExpect(jsonPath("$.cityId").value(narratorRequestDTO.getCityId().toString()))
                .andExpect(jsonPath("$.birthDate").value(narratorRequestDTO.getBirthDate().toString()))
                .andExpect(jsonPath("$.deathDate").value(narratorRequestDTO.getDeathDate().toString()))
                .andExpect(jsonPath("$.biography").value(narratorRequestDTO.getBiography()));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN", "LIBRARIAN"})
    void testDeleteNarratorById_Success() throws Exception {
        Long narratorId = 1L;

        mockMvc.perform(delete("/api/v1/narrators/{narratorId}", narratorId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string(NARRATOR_DELETED));
    }

    @Test
    void testGetAllNarratorsBySearchedParams_Success() throws Exception {
        String name = "Bulgaria";

        mockMvc.perform(get("/api/v1/narrators/search?name=", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").isNumber());
    }
}