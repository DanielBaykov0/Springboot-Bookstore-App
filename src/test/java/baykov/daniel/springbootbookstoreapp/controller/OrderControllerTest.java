package baykov.daniel.springbootbookstoreapp.controller;

import baykov.daniel.springbootbookstoreapp.entity.Order;
import baykov.daniel.springbootbookstoreapp.payload.dto.request.OrderRequestDTO;
import baykov.daniel.springbootbookstoreapp.payload.dto.response.OrderResponseDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "martin@gmail.com", roles = "USER")
    void testPrepareOrderRequest_Success() throws Exception {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setComment("Great product");
        orderRequestDTO.setPaymentMethod(Order.PaymentMethod.CARD);

        String response = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        OrderResponseDTO orderResponseDTO = objectMapper.readValue(response, OrderResponseDTO.class);

        Assertions.assertEquals(orderRequestDTO.getComment(), orderResponseDTO.getComment());
        Assertions.assertEquals(orderRequestDTO.getPaymentMethod(), orderResponseDTO.getPaymentMethod());
    }
}