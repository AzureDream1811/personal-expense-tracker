package com.azure.expensetracker.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
    int status,
    String message,
    LocalDateTime timestamp
) {
    
}
