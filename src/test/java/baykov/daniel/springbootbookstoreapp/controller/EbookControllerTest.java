package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.entity.Product;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.CommentReviewRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.EbookRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.CommentReviewResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.EbookResponseDTO;
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
import static baykov.daniel.springbootbookstoreapp.constant.Messages.EBOOK_DELETED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EbookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static EbookRequestDTO getEbookRequestDTO() {
        EbookRequestDTO ebookRequestDTO = new EbookRequestDTO();
        ebookRequestDTO.setTitle("title");
        ebookRequestDTO.setAuthorIDs(List.of(1L, 2L, 3L));
        ebookRequestDTO.setCategoryIDs(List.of(1L, 2L, 3L));
        ebookRequestDTO.setLanguage("English");
        ebookRequestDTO.setPublicationYear(2000);
        ebookRequestDTO.setDescription("Description description");
        ebookRequestDTO.setNumberOfPages(10);
        ebookRequestDTO.setISBN("978-161-729-045-9");
        ebookRequestDTO.setFileFormat("PDF");
        ebookRequestDTO.setFileSize("2.5 MB");
        ebookRequestDTO.setPrice(BigDecimal.TEN);
        return ebookRequestDTO;
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN", "LIBRARIAN"})
    void testCreateEbook_Success() throws Exception {
        EbookRequestDTO ebookRequestDTO = getEbookRequestDTO();

        String response = mockMvc.perform(post("/api/v1/ebooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ebookRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        EbookResponseDTO ebookResponseDTO = objectMapper.readValue(response, EbookResponseDTO.class);

        Assertions.assertEquals(ebookRequestDTO.getTitle(), ebookResponseDTO.getTitle());
        Assertions.assertEquals(ebookRequestDTO.getAuthorIDs().size(), ebookResponseDTO.getAuthorNames().size());
        Assertions.assertEquals(ebookRequestDTO.getCategoryIDs().size(), ebookResponseDTO.getCategoryNames().size());
        Assertions.assertEquals(ebookRequestDTO.getLanguage(), ebookResponseDTO.getLanguage());
        Assertions.assertEquals(ebookRequestDTO.getPublicationYear(), ebookResponseDTO.getPublicationYear());
        Assertions.assertEquals(ebookRequestDTO.getDescription(), ebookResponseDTO.getDescription());
        Assertions.assertEquals(ebookRequestDTO.getNumberOfPages(), ebookResponseDTO.getNumberOfPages());
        Assertions.assertEquals(ebookRequestDTO.getISBN(), ebookResponseDTO.getISBN());
        Assertions.assertEquals(ebookRequestDTO.getFileFormat(), ebookResponseDTO.getFileFormat());
        Assertions.assertEquals(ebookRequestDTO.getFileSize(), ebookResponseDTO.getFileSize());
        Assertions.assertEquals(ebookRequestDTO.getPrice(), ebookResponseDTO.getPrice());
    }

    @Test
    void testGetEbookById_Success() throws Exception {
        Long ebookId = 51L;

        String response = mockMvc.perform(get("/api/v1/ebooks/{ebookId}", ebookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        EbookResponseDTO ebookResponseDTO = objectMapper.readValue(response, EbookResponseDTO.class);

        Assertions.assertNotNull(ebookResponseDTO.getTitle());
        Assertions.assertNotNull(ebookResponseDTO.getAuthorNames());
        Assertions.assertNotNull(ebookResponseDTO.getCategoryNames());
        Assertions.assertNotNull(ebookResponseDTO.getLanguage());
        Assertions.assertNotNull(ebookResponseDTO.getPublicationYear());
        Assertions.assertNotNull(ebookResponseDTO.getDescription());
        Assertions.assertNotNull(ebookResponseDTO.getNumberOfPages());
        Assertions.assertNotNull(ebookResponseDTO.getISBN());
        Assertions.assertNotNull(ebookResponseDTO.getFileFormat());
        Assertions.assertNotNull(ebookResponseDTO.getFileSize());
        Assertions.assertNotNull(ebookResponseDTO.getPrice());
    }

    @Test
    void testGetAllEbooks_Success() throws Exception {
        mockMvc.perform(get("/api/v1/ebooks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").isNumber());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN", "LIBRARIAN"})
    void testUpdateEbookById_Success() throws Exception {
        Long ebookId = 51L;
        EbookRequestDTO ebookRequestDTO = getEbookRequestDTO();

        String response = mockMvc.perform(put("/api/v1/ebooks/{ebookId}", ebookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ebookRequestDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        EbookResponseDTO ebookResponseDTO = objectMapper.readValue(response, EbookResponseDTO.class);

        Assertions.assertEquals(ebookRequestDTO.getTitle(), ebookResponseDTO.getTitle());
        Assertions.assertEquals(ebookRequestDTO.getAuthorIDs().size(), ebookResponseDTO.getAuthorNames().size());
        Assertions.assertEquals(ebookRequestDTO.getCategoryIDs().size(), ebookResponseDTO.getCategoryNames().size());
        Assertions.assertEquals(ebookRequestDTO.getLanguage(), ebookResponseDTO.getLanguage());
        Assertions.assertEquals(ebookRequestDTO.getPublicationYear(), ebookResponseDTO.getPublicationYear());
        Assertions.assertEquals(ebookRequestDTO.getDescription(), ebookResponseDTO.getDescription());
        Assertions.assertEquals(ebookRequestDTO.getNumberOfPages(), ebookResponseDTO.getNumberOfPages());
        Assertions.assertEquals(ebookRequestDTO.getISBN(), ebookResponseDTO.getISBN());
        Assertions.assertEquals(ebookRequestDTO.getFileFormat(), ebookResponseDTO.getFileFormat());
        Assertions.assertEquals(ebookRequestDTO.getFileSize(), ebookResponseDTO.getFileSize());
        Assertions.assertEquals(ebookRequestDTO.getPrice(), ebookResponseDTO.getPrice());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN", "LIBRARIAN"})
    void testDeleteEbookById_Success() throws Exception {
        Long ebookId = 51L;

        mockMvc.perform(delete("/api/v1/ebooks/{ebookId}", ebookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string(EBOOK_DELETED));
    }

    @Test
    void testGetSearchedEbooks_Success() throws Exception {
        String title = "Title";

        mockMvc.perform(get("/api/v1/ebooks/search?title=", title))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").isNumber());
    }

    @Test
    @WithMockUser(username = "martin@gmail.com", roles = "USER")
    void testPostEbookReview_Success() throws Exception {
        CommentReviewRequestDTO commentReviewRequestDTO = new CommentReviewRequestDTO();
        commentReviewRequestDTO.setProductId(1L);
        commentReviewRequestDTO.setProductType(Product.ProductTypeEnum.EBOOK);
        commentReviewRequestDTO.setComment("Great comment");
        commentReviewRequestDTO.setRating(5);

        String response = mockMvc.perform(post("/api/v1/ebooks/reviews")
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
    void testGetAllReviewsByEbook_Success() throws Exception {
        Long ebookId = 1L;

        mockMvc.perform(get("/api/v1/ebooks/{ebookId}/reviews", ebookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").isNumber());
    }

    @Test
    void testGetFirstRatingForEbookByUser_400_NoReview() throws Exception {
        Long ebookId = 57L;
        String userEmail = "martin@gmail.com";

        mockMvc.perform(get("/api/v1/ebooks/{ebookId}/reviews/{userEmail}", ebookId, userEmail))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(USER_NO_REVIEW));
    }
}