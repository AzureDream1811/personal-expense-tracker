package com.azure.expensetracker.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters") 
        String username,
        @NotBlank @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters") 
        String password,
        @NotBlank @Size(max = 100, message = "Email must be at most 100 characters") @Email(message = "Email should be valid") 
        String email) {

}
