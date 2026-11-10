package com.azure.expensetracker.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.expensetracker.dto.request.ExpenseFilterRequest;
import com.azure.expensetracker.dto.request.ExpensePatchRequest;
import com.azure.expensetracker.dto.request.ExpenseRequest;
import com.azure.expensetracker.dto.response.ExpenseResponse;
import com.azure.expensetracker.security.CurrentUser;
import com.azure.expensetracker.service.ExpenseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final CurrentUser currentUser;

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody ExpenseRequest expenseRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expenseService.createExpense(currentUser.getId(), expenseRequest));
    }

    @GetMapping
    public ResponseEntity<Page<ExpenseResponse>> getExpenses(Pageable pageable) {
        return ResponseEntity.ok(expenseService.getExpensesByUserId(currentUser.getId(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(
            @PathVariable Long id) {

        return ResponseEntity.ok(expenseService.getExpenseById(currentUser.getId(), id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpensePatchRequest expenseRequest) {

        return ResponseEntity.ok(expenseService.updateExpense(currentUser.getId(), id, expenseRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long id) {

        expenseService.deleteExpense(currentUser.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ExpenseResponse>> filterExpenses(
            @ModelAttribute ExpenseFilterRequest filter,
            @PageableDefault(size = 10, sort = "expenseDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(expenseService.filterExpenses(currentUser.getId(), filter, pageable));
    }

}
