package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.entity.Product;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.BookRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.CommentReviewRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.BookResponseDTO;
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
import static baykov.daniel.springbootbookstoreapp.constant.Messages.BOOK_DELETED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static BookRequestDTO getBookRequestDTO() {
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("title");
        bookRequestDTO.setAuthorIDs(List.of(1L, 2L, 3L));
        bookRequestDTO.setCategoryIDs(List.of(1L, 2L, 3L));
        bookRequestDTO.setLanguage("English");
        bookRequestDTO.setPublicationYear(2000);
        bookRequestDTO.setDescription("Description description");
        bookRequestDTO.setNumberOfPages(10);
        bookRequestDTO.setISBN("978-161-729-045-9");
        bookRequestDTO.setNumberOfAvailableCopies(10);
        bookRequestDTO.setNumberOfTotalCopies(10);
        bookRequestDTO.setPrice(BigDecimal.TEN);
        return bookRequestDTO;
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN", "LIBRARIAN"})
    void testCreateBook_Success() throws Exception {
        BookRequestDTO bookRequestDTO = getBookRequestDTO();

        String response = mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        BookResponseDTO bookResponseDTO = objectMapper.readValue(response, BookResponseDTO.class);

        Assertions.assertEquals(bookRequestDTO.getTitle(), bookResponseDTO.getTitle());
        Assertions.assertEquals(bookRequestDTO.getAuthorIDs().size(), bookResponseDTO.getAuthorNames().size());
        Assertions.assertEquals(bookRequestDTO.getCategoryIDs().size(), bookResponseDTO.getCategoryNames().size());
        Assertions.assertEquals(bookRequestDTO.getLanguage(), bookResponseDTO.getLanguage());
        Assertions.assertEquals(bookRequestDTO.getPublicationYear(), bookResponseDTO.getPublicationYear());
        Assertions.assertEquals(bookRequestDTO.getDescription(), bookResponseDTO.getDescription());
        Assertions.assertEquals(bookRequestDTO.getNumberOfPages(), bookResponseDTO.getNumberOfPages());
        Assertions.assertEquals(bookRequestDTO.getISBN(), bookResponseDTO.getISBN());
        Assertions.assertEquals(bookRequestDTO.getNumberOfAvailableCopies(), bookResponseDTO.getNumberOfAvailableCopies());
        Assertions.assertEquals(bookRequestDTO.getNumberOfTotalCopies(), bookResponseDTO.getNumberOfTotalCopies());
        Assertions.assertEquals(bookRequestDTO.getPrice(), bookResponseDTO.getPrice());
    }

    @Test
    void testGetBookById_Success() throws Exception {
        Long bookId = 21L;

        String response = mockMvc.perform(get("/api/v1/books/{bookId}", bookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        BookResponseDTO bookResponseDTO = objectMapper.readValue(response, BookResponseDTO.class);

        Assertions.assertNotNull(bookResponseDTO.getTitle());
        Assertions.assertNotNull(bookResponseDTO.getAuthorNames());
        Assertions.assertNotNull(bookResponseDTO.getCategoryNames());
        Assertions.assertNotNull(bookResponseDTO.getLanguage());
        Assertions.assertNotNull(bookResponseDTO.getPublicationYear());
        Assertions.assertNotNull(bookResponseDTO.getDescription());
        Assertions.assertNotNull(bookResponseDTO.getNumberOfPages());
        Assertions.assertNotNull(bookResponseDTO.getISBN());
        Assertions.assertNotNull(bookResponseDTO.getNumberOfAvailableCopies());
        Assertions.assertNotNull(bookResponseDTO.getNumberOfTotalCopies());
        Assertions.assertNotNull(bookResponseDTO.getPrice());
    }

    @Test
    void testGetAllBooks_Success() throws Exception {
        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").isNumber());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN", "LIBRARIAN"})
    void testUpdateBookById_Success() throws Exception {
        Long bookId = 21L;
        BookRequestDTO bookRequestDTO = getBookRequestDTO();

        String response = mockMvc.perform(put("/api/v1/books/{bookId}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        BookResponseDTO bookResponseDTO = objectMapper.readValue(response, BookResponseDTO.class);

        Assertions.assertEquals(bookRequestDTO.getTitle(), bookResponseDTO.getTitle());
        Assertions.assertEquals(bookRequestDTO.getAuthorIDs().size(), bookResponseDTO.getAuthorNames().size());
        Assertions.assertEquals(bookRequestDTO.getCategoryIDs().size(), bookResponseDTO.getCategoryNames().size());
        Assertions.assertEquals(bookRequestDTO.getLanguage(), bookResponseDTO.getLanguage());
        Assertions.assertEquals(bookRequestDTO.getPublicationYear(), bookResponseDTO.getPublicationYear());
        Assertions.assertEquals(bookRequestDTO.getDescription(), bookResponseDTO.getDescription());
        Assertions.assertEquals(bookRequestDTO.getNumberOfPages(), bookResponseDTO.getNumberOfPages());
        Assertions.assertEquals(bookRequestDTO.getISBN(), bookResponseDTO.getISBN());
        Assertions.assertEquals(bookRequestDTO.getNumberOfAvailableCopies(), bookResponseDTO.getNumberOfAvailableCopies());
        Assertions.assertEquals(bookRequestDTO.getNumberOfTotalCopies(), bookResponseDTO.getNumberOfTotalCopies());
        Assertions.assertEquals(bookRequestDTO.getPrice(), bookResponseDTO.getPrice());
    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN", "LIBRARIAN"})
    void testDeleteBookById_Success() throws Exception {
        Long bookId = 21L;

        mockMvc.perform(delete("/api/v1/books/{bookId}", bookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string(BOOK_DELETED));
    }

    @Test
    void testGetSearchedBooks_Success() throws Exception {
        String title = "Title";

        mockMvc.perform(get("/api/v1/books/search?title=", title))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").isNumber());
    }

    @Test
    @WithMockUser(username = "martin@gmail.com", roles = "USER")
    void testPostBookReview_Success() throws Exception {
        CommentReviewRequestDTO commentReviewRequestDTO = new CommentReviewRequestDTO();
        commentReviewRequestDTO.setProductId(1L);
        commentReviewRequestDTO.setProductType(Product.ProductTypeEnum.BOOK);
        commentReviewRequestDTO.setComment("Great comment");
        commentReviewRequestDTO.setRating(5);

        String response = mockMvc.perform(post("/api/v1/books/reviews")
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
    void testGetAllReviewsByBook_Success() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(get("/api/v1/books/{bookId}/reviews", bookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").isNumber());
    }

    @Test
    void testGetFirstRatingForBookByUser_400_NoReview() throws Exception {
        Long bookId = 1L;
        String userEmail = "martin@gmail.com";

        mockMvc.perform(get("/api/v1/ebooks/{bookId}/reviews/{userEmail}", bookId, userEmail))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(USER_NO_REVIEW));
    }
}