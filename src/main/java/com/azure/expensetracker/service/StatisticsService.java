package com.azure.expensetracker.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azure.expensetracker.dto.request.ExpenseSummaryRequest;
import com.azure.expensetracker.dto.response.ExpenseSummaryResponse;
import com.azure.expensetracker.repository.ExpenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final ExpenseRepository expenseRepository;

    @Transactional(readOnly = true)
    public ExpenseSummaryResponse getExpenseSummary(Long userId, ExpenseSummaryRequest request) {
        return expenseRepository.getExpenseSummary(userId, request.startDate(), request.endDate());
    } 

}
