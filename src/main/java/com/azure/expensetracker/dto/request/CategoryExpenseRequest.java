package com.azure.expensetracker.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record CategoryExpenseRequest(
    @NotNull 
    LocalDate startDate,
    @NotNull 
    LocalDate endDate
) {
    
}
