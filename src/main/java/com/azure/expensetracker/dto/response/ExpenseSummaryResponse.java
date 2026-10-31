package com.azure.expensetracker.dto.response;

import java.math.BigDecimal;

public record ExpenseSummaryResponse(
    BigDecimal totalExpenses,
    Long totalTransactions,
    Double averageExpense
) {
    
}
