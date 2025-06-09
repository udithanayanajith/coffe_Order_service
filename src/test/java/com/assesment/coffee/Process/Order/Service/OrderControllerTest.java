package com.assesment.coffee.Process.Order.Service;

import com.assesment.coffee.Process.Order.Service.controller.OrderController;
import com.assesment.coffee.Process.Order.Service.dto.OrderRequest;
import com.assesment.coffee.Process.Order.Service.dto.OrderResponse;
import com.assesment.coffee.Process.Order.Service.exception.GlobalExceptionHandler;
import com.assesment.coffee.Process.Order.Service.exception.OrderProcessingException;
import com.assesment.coffee.Process.Order.Service.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testCreateOrder_Success() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setCustomerId(1L);
        request.setShopId(1L);
        request.setItemName("Latte");
        request.setItemPrice(BigDecimal.valueOf(4.99));
        request.setQueueNumber(1);

        OrderResponse mockResponse = new OrderResponse();
        mockResponse.setOrderId(1L);
        mockResponse.setItemName("Latte");
        mockResponse.setItemPrice(BigDecimal.valueOf(4.99));

        when(orderService.processOrder(any(OrderRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1L))
                .andExpect(jsonPath("$.itemName").value("Latte"))
                .andExpect(jsonPath("$.itemPrice").value(4.99));

        verify(orderService, times(1)).processOrder(any(OrderRequest.class));
    }


    @Test
    public void testGetOrderStatus_NotFound() throws Exception {
        when(orderService.getOrderStatus(anyLong()))
                .thenThrow(new OrderProcessingException("Order not found", "Order with ID 999999 not found"));

        mockMvc.perform(get("/api/v1/orders/999999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Order not found"))
                .andExpect(jsonPath("$.detail").value("Order with ID 999999 not found"));

        verify(orderService, times(1)).getOrderStatus(999999L);
    }

    @Test
    public void testCancelOrder_Success() throws Exception {
        doNothing().when(orderService).cancelOrder(anyLong());

        mockMvc.perform(delete("/api/v1/orders/1"))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).cancelOrder(1L);
    }

    @Test
    public void testCancelOrder_NotFound() throws Exception {
        doThrow(new OrderProcessingException("Order not found", "Order with ID 999 not found"))
                .when(orderService).cancelOrder(999L);

        mockMvc.perform(delete("/api/v1/orders/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Order not found"))
                .andExpect(jsonPath("$.detail").value("Order with ID 999 not found"));

        verify(orderService, times(1)).cancelOrder(999L);
    }
}