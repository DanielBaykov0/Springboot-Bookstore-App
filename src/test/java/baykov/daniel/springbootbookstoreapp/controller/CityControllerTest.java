package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.entity.City;
import baykov.daniel.springbootbookstoreapp.entity.Country;
import baykov.daniel.springbootbookstoreapp.payload.dto.CityDTO;
import baykov.daniel.springbootbookstoreapp.repository.CityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static baykov.daniel.springbootbookstoreapp.constant.Messages.CITY_DELETED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CityRepository cityRepository;


    @BeforeEach
    void setUp() {
        cityRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN", "LIBRARIAN"})
    void testCreateCity_Success() throws Exception {
        CityDTO cityDTO = new CityDTO();
        cityDTO.setName("name");
        cityDTO.setCountryId(1L);

        mockMvc.perform(post("/api/v1/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cityDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(cityDTO.getName()))
                .andExpect(jsonPath("$.countryId").value(cityDTO.getCountryId()));
    }

    @Test
    void testGetCityById_Success() throws Exception {
        City city = new City("City", new Country("Country"));
        cityRepository.save(city);

        Long cityId = 1L;

        mockMvc.perform(get("/api/v1/cities/{cityId}", cityId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.name").isNotEmpty());
    }

    @Test
    void testGetAllCities_Success() throws Exception {
        mockMvc.perform(get("/api/v1/cities"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").isNumber());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN", "LIBRARIAN"})
    void testUpdateCityById_Success() throws Exception {
        City savedCity = new City("City", new Country("Country"));
        cityRepository.save(savedCity);

        CityDTO cityDTO = new CityDTO();
        cityDTO.setName("Updated name");
        cityDTO.setCountryId(1L);

        mockMvc.perform(put("/api/v1/cities/{cityId}", savedCity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cityDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(cityDTO.getName()))
                .andExpect(jsonPath("$.countryId").value(cityDTO.getCountryId()));
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN", "LIBRARIAN"})
    void deleteCityById() throws Exception {
        City savedCity = new City("City", new Country("Country"));
        cityRepository.save(savedCity);

        mockMvc.perform(delete("/api/v1/cities/{cityId}", savedCity.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string(CITY_DELETED));
    }

    @Test
    void getAllCitiesByCountryName() throws Exception {
        City savedCity = new City("City", new Country("Country"));
        cityRepository.save(savedCity);

        mockMvc.perform(get("/api/v1/cities/search?countryName=", savedCity.getCountry().getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").isNumber());
    }
}