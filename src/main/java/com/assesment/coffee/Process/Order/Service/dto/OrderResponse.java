package com.assesment.coffee.Process.Order.Service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for the response of an order.
 * Contains details about the processed order.
 */
@Data
public class OrderResponse {

    @Schema(description = "Unique identifier of the order", example = "789")
    private Long orderId;

    @Schema(description = "Name of the customer who placed the order", example = "John Doe")
    private String customerName;

    @Schema(description = "Name of the shop where the order was placed", example = "Coffee House")
    private String shopName;

    @Schema(description = "Name of the item ordered", example = "Latte")
    private String itemName;

    @Schema(description = "Price of the item ordered", example = "4.99")
    private BigDecimal itemPrice;

    @Schema(description = "Queue number assigned to the order", example = "1")
    private Integer queueNumber;

    @Schema(description = "Position of the order in the queue", example = "2")
    private Integer queuePosition;

    @Schema(description = "Estimated wait time in minutes for the order", example = "5")
    private Integer estimatedWaitMinutes;

    @Schema(description = "Current status of the order", example = "WAITING")
    private String status;

    @Schema(description = "Timestamp when the order was created")
    private LocalDateTime createdAt;
}