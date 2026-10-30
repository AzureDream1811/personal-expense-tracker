package com.azure.expensetracker.dto.response;

public record CategoryResponse(
    Long id,
    String name,
    String description
) {
    
}
