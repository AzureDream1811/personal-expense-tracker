package com.azure.expensetracker.dto.response;

public record LoginResponse(
    String accessToken,
    String tokenType,
    Long expiresIn
) {
    
}
