package com.assesment.coffee.Process.Order.Service.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Custom exception for handling errors during order processing.
 * Extends RuntimeException to allow for unchecked exceptions.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderProcessingException extends RuntimeException {

    private final String userMessage;
    private final String technicalDetail;

    public OrderProcessingException(String userMessage, String technicalDetail) {
        super(technicalDetail);
        this.userMessage = userMessage;
        this.technicalDetail = technicalDetail;
    }
}