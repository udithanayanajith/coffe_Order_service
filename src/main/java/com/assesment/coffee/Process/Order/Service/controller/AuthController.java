package com.assesment.coffee.Process.Order.Service.controller;

import com.assesment.coffee.Process.Order.Service.dto.AuthRequest;
import com.assesment.coffee.Process.Order.Service.dto.AuthResponse;
import com.assesment.coffee.Process.Order.Service.exception.GlobalExceptionHandler;
import com.assesment.coffee.Process.Order.Service.util.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * Controller for handling user authentication and authorization.
 * Provides endpoints for user login and JWT token generation.
 * Secures all other endpoints by requiring valid JWT tokens.
 */
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    /**
     * Authenticates the user and issues a JWT token on success.
     * Accepts username and password, validates credentials, and returns a signed token.
     *
     * @param authRequest the login credentials (username and password)
     * @return the response containing a JWT token or an error message
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            if ("admin".equals(authRequest.getUsername()) && "password123".equals(authRequest.getPassword())) {
                String token = jwtUtil.generateToken(authRequest.getUsername());
                return ResponseEntity.ok(new AuthResponse(token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new GlobalExceptionHandler.ErrorResponse(
                                "Invalid username or password",
                                "Authentication failed",
                                Instant.now(),
                                "/api/v1/auth/login"
                        ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GlobalExceptionHandler.ErrorResponse(
                            "Login failed",
                            e.getMessage(),
                            Instant.now(),
                            "/api/v1/auth/login"
                    ));
        }
    }
}