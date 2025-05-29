package com.assesment.coffee.Process.Order.Service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for the response of queue status.
 * Contains details about the queue length and estimated wait time.
 */
@Data
public class QueueStatusResponse {

    @Schema(description = "Queue number being queried", example = "1")
    private Integer queueNumber;

    @Schema(description = "Current length of the queue", example = "5")
    private Integer queueLength;

    @Schema(description = "Estimated wait time in minutes for the queue", example = "10")
    private Integer estimatedWaitMinutes;
}