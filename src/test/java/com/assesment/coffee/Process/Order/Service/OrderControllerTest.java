package com.assesment.coffee.Process.Order.Service;

import com.assesment.coffee.Process.Order.Service.dto.OrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateOrder() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setCustomerId(1L);
        request.setShopId(1L);
        request.setItemName("Latte");
        request.setItemPrice(4.99f);
        request.setQueueNumber(1);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.itemName").value("Latte"))
                .andExpect(jsonPath("$.itemPrice").value(4.99));
    }

    @Test
    public void testCreateOrderWithInvalidData() throws Exception {
        // Missing required fields
        String invalidOrderJson = """
            {
                "customerId": null,
                "shopId": 1,
                "itemName": "",
                "itemPrice": -1.0,
                "queueNumber": 4
            }
        """;

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidOrderJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    public void testGetOrderNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/orders/11")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}