package com.azure.expensetracker.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ExpenseRequest(
    @NotNull 
    @DecimalMin("0.01")
    BigDecimal amount,

    @Size(max = 255)
    String description,

    @NotNull 
    LocalDate expenseDate,

    @NotNull
    @Positive 
    Long categoryId
) {
    
}
