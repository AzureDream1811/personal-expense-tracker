package com.azure.expensetracker.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azure.expensetracker.dto.request.ExpensePatchRequest;
import com.azure.expensetracker.dto.request.ExpenseRequest;
import com.azure.expensetracker.dto.response.ExpenseResponse;
import com.azure.expensetracker.entity.Category;
import com.azure.expensetracker.entity.Expense;
import com.azure.expensetracker.entity.User;
import com.azure.expensetracker.exception.ResourceNotFoundException;
import com.azure.expensetracker.repository.CategoryRepository;
import com.azure.expensetracker.repository.ExpenseRepository;
import com.azure.expensetracker.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public ExpenseResponse createExpense(Long userId, ExpenseRequest expenseRequest) {
        User user = getUserById(userId);
        Category category = getCategoryById(expenseRequest.categoryId());

        Expense expense = Expense.builder()
                .amount(expenseRequest.amount())
                .description(expenseRequest.description())
                .expenseDate(expenseRequest.expenseDate())
                .user(user)
                .category(category)
                .build();
        Expense savedExpense = expenseRepository.save(expense);

        return mapToExpenseResponse(savedExpense);
    }

    @Transactional(readOnly = true)
    public ExpenseResponse getExpenseById(Long id) {
        Expense expense = expenseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with ID: " + id));

        return mapToExpenseResponse(expense);
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> getAllExpenses() {

        return expenseRepository.findAllWithCategory()
                .stream()
                .map(this::mapToExpenseResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<ExpenseResponse> getExpensesByUserId(Long userId, Pageable pageable) {
        return expenseRepository.findByUser_IdAndDeletedAtIsNull(userId, pageable)
                .map(this::mapToExpenseResponse);
    }

    @Transactional
    public ExpenseResponse updateExpense(Long id, ExpensePatchRequest expenseRequest) {
        Expense expense = expenseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with ID: " + id));

        if (expenseRequest.amount() != null) {
            expense.setAmount(expenseRequest.amount());
        }

        if (expenseRequest.description() != null) {
            expense.setDescription(expenseRequest.description());
        }

        if (expenseRequest.expenseDate() != null) {
            expense.setExpenseDate(expenseRequest.expenseDate());
        }

        if (expenseRequest.categoryId() != null) {
            Category category = getCategoryById(expenseRequest.categoryId());
            expense.setCategory(category);
        }

        return mapToExpenseResponse(expense);
    }

    @Transactional
    public void deleteExpense(Long id) {
        Expense expense = expenseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with ID: " + id));

        expense.setDeletedAt(LocalDateTime.now());
    }

    private User getUserById(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findByIdAndDeletedAtIsNull(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));
    }

    private ExpenseResponse mapToExpenseResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getAmount(),
                expense.getDescription(),
                expense.getExpenseDate(),
                expense.getCategory().getId(),
                expense.getCategory().getName(),
                expense.getCreatedAt(),
                expense.getUpdatedAt());
    }
}
