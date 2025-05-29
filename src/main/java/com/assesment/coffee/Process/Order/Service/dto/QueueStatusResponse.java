package com.assesment.coffee.Process.Order.Service.dto;

import lombok.Data;

@Data
public class QueueStatusResponse {
    private Integer queueNumber;
    private Integer queueLength;
    private Integer estimatedWaitMinutes;
}