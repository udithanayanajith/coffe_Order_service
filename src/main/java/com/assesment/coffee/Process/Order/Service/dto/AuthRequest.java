package com.assesment.coffee.Process.Order.Service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object for authentication requests.
 * Contains username and password fields for user login.
 * Includes validation annotations for required fields.
 */
@Data
public class AuthRequest {
    @NotBlank(message = "Username is required")
    @Schema(description = "Username of customer : admin", example = "admin")
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(description = "Password of customer : password123", example = "password123")
    private String password;
}

