package com.assesment.coffee.Process.Order.Service.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for authentication failures.
 * Includes user-friendly message and technical details.
 */
@EqualsAndHashCode(callSuper = true)
@ResponseStatus(HttpStatus.UNAUTHORIZED)
@Data
public class AuthenticationException extends RuntimeException {
    private final String userMessage;
    private final String technicalDetail;

    public AuthenticationException(String userMessage, String technicalDetail) {
        super(technicalDetail);
        this.userMessage = userMessage;
        this.technicalDetail = technicalDetail;
    }
}