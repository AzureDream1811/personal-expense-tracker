package com.azure.expensetracker.dto.response;

import java.math.BigDecimal;

public record CategoryExpenseResponse(
        Long categoryId,
        String categoryName,
        BigDecimal totalAmount,
        Long totalTransactions) {

}
