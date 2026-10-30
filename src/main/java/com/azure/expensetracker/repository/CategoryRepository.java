package com.azure.expensetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azure.expensetracker.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}
