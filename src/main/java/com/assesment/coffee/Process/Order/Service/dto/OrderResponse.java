package com.assesment.coffee.Process.Order.Service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderResponse {
    private Long orderId;
    private String customerName;
    private String shopName;
    private String itemName;
    private Double itemPrice;
    private Integer queueNumber;
    private Integer queuePosition;
    private Integer estimatedWaitMinutes;
    private String status;
    private LocalDateTime createdAt;
}