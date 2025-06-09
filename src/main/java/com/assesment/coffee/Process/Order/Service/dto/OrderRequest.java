package com.assesment.coffee.Process.Order.Service.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for creating a new coffee order request.
 * Validates the input data for order processing.
 */
@Data
public class OrderRequest {

    @NotNull
    @Schema(description = "ID of the customer placing the order", example = "123")
    private Long customerId;

    @NotNull
    @Schema(description = "ID of the shop where the order is to be placed", example = "456")
    private Long shopId;

    @NotBlank
    @Schema(description = "Name of the item being ordered", example = "Latte")
    private String itemName;

    @Positive
    @Schema(description = "Price of the item being ordered", example = "4.99")
    private BigDecimal itemPrice;

    @Min(1)
    @Max(3)
    @Schema(description = "Queue number for the order (default is 1)", example = "1")
    private Integer queueNumber = 1;
}