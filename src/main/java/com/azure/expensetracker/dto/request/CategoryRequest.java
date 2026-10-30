package com.azure.expensetracker.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
    @NotNull 
    @Size(max = 100)
    String name,
    @Size(max = 255)
    String description
) {
    
}
