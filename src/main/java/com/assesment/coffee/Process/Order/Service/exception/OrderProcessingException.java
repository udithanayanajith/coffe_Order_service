package com.assesment.coffee.Process.Order.Service.exception;


/**
 * Custom exception for handling errors during order processing.
 * Extends RuntimeException to allow for unchecked exceptions.
 */
public class OrderProcessingException extends RuntimeException {
    public OrderProcessingException(String message) {
        super(message);
    }
}