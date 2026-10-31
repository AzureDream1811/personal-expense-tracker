package com.azure.expensetracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.azure.expensetracker.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

        @EntityGraph(attributePaths = "category")
        Optional<Expense> findByIdAndDeletedAtIsNull(Long id);

        @EntityGraph(attributePaths = "category")
        List<Expense> findAllByDeletedAtIsNull();

        @EntityGraph(attributePaths = "category")
        Page<Expense> findByUser_IdAndDeletedAtIsNull(
                        Long userId,
                        Pageable pageable);

        @EntityGraph(attributePaths = "category")
        Optional<Expense> findByIdAndUser_IdAndDeletedAtIsNull(
                        Long id,
                        Long userId);

}
