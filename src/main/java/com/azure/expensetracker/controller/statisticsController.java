package com.azure.expensetracker.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azure.expensetracker.dto.request.CategoryExpenseRequest;
import com.azure.expensetracker.dto.request.ExpenseSummaryRequest;
import com.azure.expensetracker.dto.response.CategoryExpenseResponse;
import com.azure.expensetracker.dto.response.ExpenseSummaryResponse;
import com.azure.expensetracker.service.StatisticsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
public class statisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/summary")
    public ResponseEntity<ExpenseSummaryResponse> getExpenseSummary(
            @RequestParam Long userId,
            @Valid @ModelAttribute ExpenseSummaryRequest request) {

        return ResponseEntity.ok(
                statisticsService.getExpenseSummary(userId, request));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryExpenseResponse>> getCategoryExpense(
            @RequestParam Long userId,
            @Valid @ModelAttribute CategoryExpenseRequest request) {

        return ResponseEntity.ok(statisticsService.getCategoryExpense(userId, request));
    }

}
