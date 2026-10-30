package com.azure.expensetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azure.expensetracker.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
}
