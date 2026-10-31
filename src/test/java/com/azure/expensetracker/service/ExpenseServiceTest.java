package com.azure.expensetracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.azure.expensetracker.dto.request.ExpenseRequest;
import com.azure.expensetracker.dto.response.ExpenseResponse;
import com.azure.expensetracker.entity.Category;
import com.azure.expensetracker.entity.Expense;
import com.azure.expensetracker.entity.User;
import com.azure.expensetracker.exception.ResourceNotFoundException;
import com.azure.expensetracker.repository.CategoryRepository;
import com.azure.expensetracker.repository.ExpenseRepository;
import com.azure.expensetracker.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private Long userId;
    private Long categoryId;

    private User user;
    private Category category;
    private ExpenseRequest expenseRequest;

    @BeforeEach
    void setUp() {
        userId = 1L;
        user = new User();
        user.setId(userId);

        categoryId = 1L;
        category = new Category();
        category.setId(categoryId);
        category.setName("Test Category");
        category.setUser(user);

        expenseRequest = new ExpenseRequest(
                BigDecimal.valueOf(100.0),
                "Test Expense",
                LocalDate.now(),
                categoryId);
    }

    @Test
    void createExpense_ShouldThrowResourceNotFoundException_WhenUserNotFound() {
        when(userRepository.findByIdAndDeletedAtIsNull(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> expenseService.createExpense(userId, expenseRequest));

        assertEquals("User not found with ID: " + userId, ex.getMessage());

        // Verify side effects
        verify(expenseRepository, never()).save(any());
        verifyNoInteractions(categoryRepository);
    }

    @Test
    void createExpense_ShouldReturnCorrectExpenseResponse_WhenUserAndCategoryExist() {
        Expense savedExpense = Expense.builder()
                .id(10L)
                .amount(expenseRequest.amount())
                .description(expenseRequest.description())
                .expenseDate(expenseRequest.expenseDate())
                .user(user)
                .category(category)
                .build();

        // Mock user and category retrieval
        when(userRepository.findByIdAndDeletedAtIsNull(userId))
                .thenReturn(Optional.of(user));

        when(categoryRepository.findByIdAndUser_IdAndDeletedAtIsNull(categoryId, userId))
                .thenReturn(Optional.of(category));

        when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);

        // Act
        ExpenseResponse response = expenseService.createExpense(userId, expenseRequest);
        assertEquals(savedExpense.getId(), response.id());
        assertEquals(expenseRequest.amount(), response.amount());
        assertEquals(expenseRequest.description(), response.description());
        assertEquals(categoryId, response.categoryId());
        assertEquals(category.getName(), response.categoryName());

        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    void getExpenseById_ShouldThrowResourceNotFoundException_WhenExpenseNotFound() {
        Long expenseId = 10L;
        when(expenseRepository.findByIdAndUser_IdAndDeletedAtIsNull(expenseId, userId))
                .thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> expenseService.getExpenseById(userId, expenseId));

        assertEquals("Expense not found with ID: " + expenseId, ex.getMessage());

        verify(expenseRepository).findByIdAndUser_IdAndDeletedAtIsNull(expenseId, userId);
    }

    @Test 
    void getExpenseById_ShouldReturnCorrectExpenseResponse_WhenExpenseExists() {
        Long expenseId = 10L;
        Expense expense = Expense.builder()
                .id(expenseId)
                .amount(BigDecimal.valueOf(100.0))
                .description("Test Expense")
                .expenseDate(LocalDate.now())
                .user(user)
                .category(category)
                .build();

        when(expenseRepository.findByIdAndUser_IdAndDeletedAtIsNull(expenseId, userId))
                .thenReturn(Optional.of(expense));

        // Act
        ExpenseResponse response = expenseService.getExpenseById(userId, expenseId);

        // Assert
        assertEquals(expense.getId(), response.id());
        assertEquals(expense.getAmount(), response.amount());
        assertEquals(expense.getDescription(), response.description());
        assertEquals(category.getId(), response.categoryId());
        assertEquals(category.getName(), response.categoryName());
        
        verify(expenseRepository).findByIdAndUser_IdAndDeletedAtIsNull(expenseId, userId);
    }
}