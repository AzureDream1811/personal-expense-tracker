package com.azure.expensetracker.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azure.expensetracker.dto.request.ExpenseSummaryRequest;
import com.azure.expensetracker.dto.response.ExpenseSummaryResponse;
import com.azure.expensetracker.service.StatisticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
public class statisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/summary")
    public ResponseEntity<ExpenseSummaryResponse> getExpenseSummary(
            @RequestParam Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        ExpenseSummaryRequest request = new ExpenseSummaryRequest(
                LocalDate.parse(startDate), LocalDate.parse(endDate));

        return ResponseEntity.ok(
                statisticsService.getExpenseSummary(userId, request));
    }

}
