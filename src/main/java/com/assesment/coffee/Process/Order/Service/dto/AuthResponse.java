package com.assesment.coffee.Process.Order.Service.dto;


import lombok.Data;
/**
 * Data Transfer Object for authentication responses.
 * Contains the JWT token generated upon successful authentication.
 * Returned to the client after successful login.
 */
@Data
public class AuthResponse {
    private final String token;

    public AuthResponse(String token) {
        this.token = token;
    }
}