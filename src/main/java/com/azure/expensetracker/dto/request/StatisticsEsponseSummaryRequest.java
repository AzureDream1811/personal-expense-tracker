package com.azure.expensetracker.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record StatisticsEsponseSummaryRequest(
    @NotNull
    LocalDate startDate,
    @NotNull 
    LocalDate endDate
) {
    
}
