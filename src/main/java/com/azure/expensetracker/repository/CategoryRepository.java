package com.azure.expensetracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azure.expensetracker.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByIdAndUser_IdAndDeletedAtIsNull(Long categoryId, Long userId);

    List<Category> findAllByUser_IdAndDeletedAtIsNull(Long userId);

    boolean existsByUser_IdAndNameAndDeletedAtIsNull(Long userId, String name);
}
