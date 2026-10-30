package com.azure.expensetracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azure.expensetracker.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByIdAndDeletedAtIsNull(Long id);

    List<Category> findAllByDeletedAtIsNull();
}
