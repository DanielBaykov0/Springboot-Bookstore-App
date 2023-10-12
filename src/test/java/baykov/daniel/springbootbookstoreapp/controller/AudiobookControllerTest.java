package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.entity.Product;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.AudiobookRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.CommentReviewRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.AudiobookResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.CommentReviewResponseDTO;
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

import java.math.BigDecimal;
import java.util.List;

import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.USER_NO_REVIEW;
import static baykov.daniel.springbootbookstoreapp.constant.Messages.AUDIOBOOK_DELETED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AudiobookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static AudiobookRequestDTO getAudiobookRequestDTO() {
        AudiobookRequestDTO audiobookRequestDTO = new AudiobookRequestDTO();
        audiobookRequestDTO.setTitle("title");
        audiobookRequestDTO.setAuthorIDs(List.of(1L, 2L, 3L));
        audiobookRequestDTO.setCategoryIDs(List.of(1L, 2L, 3L));
        audiobookRequestDTO.setNarratorId(1L);
        audiobookRequestDTO.setLanguage("English");
        audiobookRequestDTO.setPublicationYear(2000);
        audiobookRequestDTO.setDescription("Description description");
        audiobookRequestDTO.setDuration(BigDecimal.TEN);
        audiobookRequestDTO.setISBN("978-161-729-045-9");
        audiobookRequestDTO.setFileFormat("PDF");
        audiobookRequestDTO.setFileSize("2.5 MB");
        audiobookRequestDTO.setPrice(BigDecimal.TEN);
        return audiobookRequestDTO;
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN", "LIBRARIAN"})
    void testCreateAudiobook_Success() throws Exception {
        AudiobookRequestDTO audiobookRequestDTO = getAudiobookRequestDTO();

        String response = mockMvc.perform(post("/api/v1/audiobooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(audiobookRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        AudiobookResponseDTO audiobookResponseDTO = objectMapper.readValue(response, AudiobookResponseDTO.class);

        Assertions.assertEquals(audiobookRequestDTO.getTitle(), audiobookResponseDTO.getTitle());
        Assertions.assertEquals(audiobookRequestDTO.getAuthorIDs().size(), audiobookResponseDTO.getAuthorNames().size());
        Assertions.assertEquals(audiobookRequestDTO.getCategoryIDs().size(), audiobookResponseDTO.getCategoryNames().size());
        Assertions.assertEquals(audiobookRequestDTO.getNarratorId(), audiobookResponseDTO.getNarratorId());
        Assertions.assertEquals(audiobookRequestDTO.getLanguage(), audiobookResponseDTO.getLanguage());
        Assertions.assertEquals(audiobookRequestDTO.getPublicationYear(), audiobookResponseDTO.getPublicationYear());
        Assertions.assertEquals(audiobookRequestDTO.getDescription(), audiobookResponseDTO.getDescription());
        Assertions.assertEquals(audiobookRequestDTO.getDuration(), audiobookResponseDTO.getDuration());
        Assertions.assertEquals(audiobookRequestDTO.getISBN(), audiobookResponseDTO.getISBN());
        Assertions.assertEquals(audiobookRequestDTO.getFileFormat(), audiobookResponseDTO.getFileFormat());
        Assertions.assertEquals(audiobookRequestDTO.getFileSize(), audiobookResponseDTO.getFileSize());
        Assertions.assertEquals(audiobookRequestDTO.getPrice(), audiobookResponseDTO.getPrice());
    }

    @Test
    void testGetAudiobookById_Success() throws Exception {
        Long audiobookId = 1L;

        String response = mockMvc.perform(get("/api/v1/audiobooks/{audiobookId}", audiobookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        AudiobookResponseDTO audiobookResponseDTO = objectMapper.readValue(response, AudiobookResponseDTO.class);

        Assertions.assertNotNull(audiobookResponseDTO.getTitle());
        Assertions.assertNotNull(audiobookResponseDTO.getAuthorNames());
        Assertions.assertNotNull(audiobookResponseDTO.getCategoryNames());
        Assertions.assertNotNull(audiobookResponseDTO.getNarratorId());
        Assertions.assertNotNull(audiobookResponseDTO.getLanguage());
        Assertions.assertNotNull(audiobookResponseDTO.getPublicationYear());
        Assertions.assertNotNull(audiobookResponseDTO.getDescription());
        Assertions.assertNotNull(audiobookResponseDTO.getDuration());
        Assertions.assertNotNull(audiobookResponseDTO.getISBN());
        Assertions.assertNotNull(audiobookResponseDTO.getFileFormat());
        Assertions.assertNotNull(audiobookResponseDTO.getFileSize());
        Assertions.assertNotNull(audiobookResponseDTO.getPrice());
    }

    @Test
    void testGetAllAudiobooks_Success() throws Exception {
        mockMvc.perform(get("/api/v1/audiobooks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").isNumber());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN", "LIBRARIAN"})
    void testUpdateAudiobookById_Success() throws Exception {
        Long audiobookId = 1L;
        AudiobookRequestDTO audiobookRequestDTO = getAudiobookRequestDTO();

        String response = mockMvc.perform(put("/api/v1/audiobooks/{audiobookId}", audiobookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(audiobookRequestDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        AudiobookResponseDTO audiobookResponseDTO = objectMapper.readValue(response, AudiobookResponseDTO.class);

        Assertions.assertEquals(audiobookRequestDTO.getTitle(), audiobookResponseDTO.getTitle());
        Assertions.assertEquals(audiobookRequestDTO.getAuthorIDs().size(), audiobookResponseDTO.getAuthorNames().size());
        Assertions.assertEquals(audiobookRequestDTO.getCategoryIDs().size(), audiobookResponseDTO.getCategoryNames().size());
        Assertions.assertEquals(audiobookRequestDTO.getNarratorId(), audiobookResponseDTO.getNarratorId());
        Assertions.assertEquals(audiobookRequestDTO.getLanguage(), audiobookResponseDTO.getLanguage());
        Assertions.assertEquals(audiobookRequestDTO.getPublicationYear(), audiobookResponseDTO.getPublicationYear());
        Assertions.assertEquals(audiobookRequestDTO.getDescription(), audiobookResponseDTO.getDescription());
        Assertions.assertEquals(audiobookRequestDTO.getDuration(), audiobookResponseDTO.getDuration());
        Assertions.assertEquals(audiobookRequestDTO.getISBN(), audiobookResponseDTO.getISBN());
        Assertions.assertEquals(audiobookRequestDTO.getFileFormat(), audiobookResponseDTO.getFileFormat());
        Assertions.assertEquals(audiobookRequestDTO.getFileSize(), audiobookResponseDTO.getFileSize());
        Assertions.assertEquals(audiobookRequestDTO.getPrice(), audiobookResponseDTO.getPrice());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN", "LIBRARIAN"})
    void testDeleteAudiobookById_Success() throws Exception {
        Long audiobookId = 1L;

        mockMvc.perform(delete("/api/v1/audiobooks/{audiobookId}", audiobookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string(AUDIOBOOK_DELETED));
    }

    @Test
    void testGetSearchedAudiobooks_Success() throws Exception {
        String title = "Title";

        mockMvc.perform(get("/api/v1/audiobooks/search?title=", title))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").isNumber());
    }

    @Test
    @WithMockUser(username = "martin@gmail.com", roles = "USER")
    void testPostAudiobookReview_Success() throws Exception {
        CommentReviewRequestDTO commentReviewRequestDTO = new CommentReviewRequestDTO();
        commentReviewRequestDTO.setProductId(1L);
        commentReviewRequestDTO.setProductType(Product.ProductTypeEnum.AUDIOBOOK);
        commentReviewRequestDTO.setComment("Great comment");
        commentReviewRequestDTO.setRating(5);

        String response = mockMvc.perform(post("/api/v1/audiobooks/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentReviewRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        CommentReviewResponseDTO commentReviewResponseDTO = objectMapper.readValue(response, CommentReviewResponseDTO.class);

        Assertions.assertEquals(commentReviewRequestDTO.getRating(), commentReviewResponseDTO.getRating());
        Assertions.assertEquals(commentReviewRequestDTO.getComment(), commentReviewResponseDTO.getComment());
    }

    @Test
    void testGetAllReviewsByAudiobook_Success() throws Exception {
        Long audiobookId = 1L;

        mockMvc.perform(get("/api/v1/audiobooks/{audiobookId}/reviews", audiobookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").isNumber());
    }

    @Test
    void testGetFirstRatingForAudiobookByUser_400_NoReview() throws Exception {
        Long audiobookId = 47L;
        String userEmail = "martin@gmail.com";

        mockMvc.perform(get("/api/v1/audiobooks/{audiobookId}/reviews/{userEmail}", audiobookId, userEmail))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(USER_NO_REVIEW));
    }
}