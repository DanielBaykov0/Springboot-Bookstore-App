package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.entity.Product;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.CartRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.CartResponseDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.ProductResponseDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "martin@gmail.com", roles = "USER")
    void testAddToCart_Success() throws Exception {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setProductId(11L);
        cartRequestDTO.setQuantity(1);
        cartRequestDTO.setProductType(Product.ProductTypeEnum.AUDIOBOOK);

        String response = mockMvc.perform(post("/api/v1/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        CartResponseDTO cartResponseDTO = objectMapper.readValue(response, CartResponseDTO.class);

        Assertions.assertEquals(cartRequestDTO.getQuantity(), cartResponseDTO.getProducts().stream().map(ProductResponseDTO::getQuantity).toList().get(0));
        Assertions.assertEquals(cartRequestDTO.getProductType(), cartResponseDTO.getProducts().stream().map(ProductResponseDTO::getProductType).toList().get(0));
    }

    @Test
    @WithMockUser(username = "martin@gmail.com", roles = "USER")
    void testRemoveFromCart_Success() throws Exception {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setProductId(1L);
        cartRequestDTO.setQuantity(1);
        cartRequestDTO.setProductType(Product.ProductTypeEnum.AUDIOBOOK);

        String response = mockMvc.perform(post("/api/v1/cart/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartRequestDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        CartResponseDTO cartResponseDTO = objectMapper.readValue(response, CartResponseDTO.class);

        Assertions.assertEquals(cartRequestDTO.getQuantity(), cartResponseDTO.getProducts().stream().map(ProductResponseDTO::getQuantity).toList().get(0));
        Assertions.assertEquals(cartRequestDTO.getProductType(), cartResponseDTO.getProducts().stream().map(ProductResponseDTO::getProductType).toList().get(0));
    }

    @Test
    @WithMockUser(username = "martin@gmail.com", roles = "USER")
    void testShowUserCart_Success() throws Exception {
        String response = mockMvc.perform(get("/api/v1/cart"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        CartResponseDTO cartResponseDTO = objectMapper.readValue(response, CartResponseDTO.class);

        Assertions.assertNotNull(cartResponseDTO.getProducts());
        Assertions.assertNotNull(cartResponseDTO.getProductsCount());
        Assertions.assertNotNull(cartResponseDTO.getProductsSum());
    }
}