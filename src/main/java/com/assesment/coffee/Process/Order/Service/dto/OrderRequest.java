package com.assesment.coffee.Process.Order.Service.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrderRequest {
    @NotNull
    private Long customerId;

    @NotNull
    private Long shopId;

    @NotBlank
    private String itemName;

    @Positive
    private Double itemPrice;

    @Min(1)
    @Max(3)
    private Integer queueNumber = 1;
}