package com.azure.expensetracker.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseFilterRequest(
        Long categoryId,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        String description) {

}
