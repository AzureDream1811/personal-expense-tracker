package com.azure.expensetracker.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.azure.expensetracker.dto.response.ExpenseResponse;
import com.azure.expensetracker.service.ExpenseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<Page<ExpenseResponse>> getExpense(@RequestParam Long userId, Pageable pageable) {
        return ResponseEntity.ok(expenseService.getExpensesByUserId(userId, pageable));
    }
}
