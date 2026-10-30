package com.azure.expensetracker.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azure.expensetracker.dto.request.ExpensePatchRequest;
import com.azure.expensetracker.dto.request.ExpenseRequest;
import com.azure.expensetracker.dto.response.ExpenseResponse;
import com.azure.expensetracker.service.ExpenseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @RequestParam Long userId,
            @Valid @RequestBody ExpenseRequest expenseRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expenseService.createExpense(userId, expenseRequest));
    }

    @GetMapping
    public ResponseEntity<Page<ExpenseResponse>> getExpense(@RequestParam Long userId, Pageable pageable) {
        return ResponseEntity.ok(expenseService.getExpensesByUserId(userId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(
            @RequestParam Long userId, @PathVariable Long id) {

        return ResponseEntity.ok(expenseService.getExpenseById(userId, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @RequestParam Long userId, @PathVariable Long id,
            @Valid @RequestBody ExpensePatchRequest expenseRequest) {

        return ResponseEntity.ok(expenseService.updateExpense(userId, id, expenseRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @RequestParam Long userId, @PathVariable Long id) {

        expenseService.deleteExpense(userId, id);
        return ResponseEntity.noContent().build();
    }
}
