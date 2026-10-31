package com.azure.expensetracker.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azure.expensetracker.dto.request.ExpenseFilterRequest;
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

import jakarta.persistence.criteria.Predicate;
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
        Category category = getCategoryById(userId, expenseRequest.categoryId());

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
    public ExpenseResponse getExpenseById(Long userId, Long id) {
        Expense expense = expenseRepository.findByIdAndUser_IdAndDeletedAtIsNull(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with ID: " + id));

        return mapToExpenseResponse(expense);
    }

    @Transactional(readOnly = true)
    public Page<ExpenseResponse> getExpensesByUserId(Long userId, Pageable pageable) {
        return expenseRepository.findByUser_IdAndDeletedAtIsNull(userId, pageable)
                .map(this::mapToExpenseResponse);
    }

    @Transactional
    public ExpenseResponse updateExpense(Long userId, Long id, ExpensePatchRequest expenseRequest) {
        Expense expense = expenseRepository.findByIdAndUser_IdAndDeletedAtIsNull(id, userId)
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
            Category category = getCategoryById(userId, expenseRequest.categoryId());
            expense.setCategory(category);
        }

        return mapToExpenseResponse(expense);
    }

    @Transactional
    public void deleteExpense(Long userId, Long id) {
        Expense expense = expenseRepository.findByIdAndUser_IdAndDeletedAtIsNull(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with ID: " + id));

        expense.setDeletedAt(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public Page<ExpenseResponse> filterExpenses(
            Long userId, ExpenseFilterRequest filter, Pageable pageable) {

        validateFilters(userId, filter);

        Specification<Expense> spec = buildExpenseSpecification(userId, filter);

        return expenseRepository.findAll(spec, pageable)
                .map(this::mapToExpenseResponse);
    }

    private User getUserById(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    private Category getCategoryById(Long userId, Long categoryId) {
        return categoryRepository.findByIdAndUser_IdAndDeletedAtIsNull(categoryId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));
    }

    private void validateFilters(Long userId, ExpenseFilterRequest filter) {
        if (filter.startDate() != null
                && filter.endDate() != null
                && filter.startDate().isAfter(filter.endDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        if (filter.minAmount() != null
                && filter.maxAmount() != null
                && filter.minAmount().compareTo(filter.maxAmount()) > 0) {
            throw new IllegalArgumentException("Minimum amount cannot be greater than maximum amount");
        }

        if (filter.categoryId() != null) {
            getCategoryById(userId, filter.categoryId());
        }
    }

    private Specification<Expense> buildExpenseSpecification(
            Long userId,
            ExpenseFilterRequest filter) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    cb.equal(root.get("user").get("id"), userId));

            predicates.add(
                    cb.isNull(root.get("deletedAt")));

            if (filter.categoryId() != null) {
                predicates.add(
                        cb.equal(root.get("category").get("id"), filter.categoryId()));
            }

            if (filter.startDate() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("expenseDate"),
                                filter.startDate()));
            }

            if (filter.endDate() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("expenseDate"),
                                filter.endDate()));
            }

            if (filter.minAmount() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("amount"),
                                filter.minAmount()));
            }

            if (filter.maxAmount() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("amount"),
                                filter.maxAmount()));
            }

            if (filter.description() != null
                    && !filter.description().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("description")),
                                "%" + filter.description().toLowerCase(Locale.ROOT) + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
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
