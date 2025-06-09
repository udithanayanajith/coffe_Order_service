package com.assesment.coffee.Process.Order.Service.service;

import com.assesment.coffee.Process.Order.Service.exception.AuthenticationException;
import com.assesment.coffee.Process.Order.Service.util.JwtUtil;
import org.springframework.stereotype.Service;

/**
 * Service for JWT token validation operations.
 * Handles token extraction from header and validation logic.
 * Throws AuthenticationException for invalid/missing tokens.
 */
@Service
public class TokenValidationService {

    private final JwtUtil jwtUtil;

    public TokenValidationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Validates the Authorization header containing JWT token.
     * @param authorizationHeader The "Bearer <token>" string from the header
     * @throws AuthenticationException if token is invalid, expired or missing
     */
    public void validateAuthorizationHeader(String authorizationHeader) throws AuthenticationException {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new AuthenticationException(
                    "Authorization header required",
                    "Missing Authorization header"
            );
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new AuthenticationException(
                    "Invalid token format",
                    "Authorization header must start with 'Bearer '"
            );
        }

        String token = authorizationHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            throw new AuthenticationException(
                    "Invalid or expired token",
                    "JWT validation failed"
            );
        }
    }

    /**
     * Extracts and validates the JWT token from Authorization header.
     * @param authorizationHeader The "Bearer <token>" string from the header
     * @return The username extracted from the valid token
     * @throws AuthenticationException if token is invalid or missing
     */
    public String getUsernameFromValidatedHeader(String authorizationHeader) {
        validateAuthorizationHeader(authorizationHeader);
        return jwtUtil.getUsernameFromToken(authorizationHeader.substring(7));
    }
}