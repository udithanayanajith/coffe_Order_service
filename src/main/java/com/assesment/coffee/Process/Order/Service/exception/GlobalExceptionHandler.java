package com.assesment.coffee.Process.Order.Service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.UUID;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderProcessingException.class)
    public ResponseEntity<ErrorResponse> handleOrderProcessingException(
            OrderProcessingException ex, WebRequest request) {

        log.warn("Business error: {}", ex.getTechnicalDetail());

        return ResponseEntity.badRequest().body(
                new ErrorResponse(
                        ex.getUserMessage(),
                        ex.getTechnicalDetail(),
                        Instant.now(),
                        request.getDescription(false)
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedErrors(
            Exception ex, WebRequest request) {

        log.error("Unhandled exception", ex);

        return ResponseEntity.internalServerError().body(
                new ErrorResponse(
                        "An unexpected error occurred",
                        "Server error reference: " + UUID.randomUUID(),
                        Instant.now(),
                        request.getDescription(false)
                )
        );
    }

    @Data
    @AllArgsConstructor
    public static class ErrorResponse {
        private String message;
        private String detail;
        private Instant timestamp;
        private String path;
    }
}