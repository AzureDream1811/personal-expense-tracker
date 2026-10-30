package com.azure.expensetracker.dto.response;

public record UserResponse(
    Long id,
    String username,
    String email
) {
    
}
