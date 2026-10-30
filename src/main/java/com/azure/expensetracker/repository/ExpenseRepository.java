package com.azure.expensetracker.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.azure.expensetracker.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

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

        @Query("""
                        SELECT e FROM Expense e
                                WHERE e.user.id = :userId
                                AND e.deletedAt IS NULL
                                AND (:categoryId IS NULL OR e.category.id = :categoryId)
                                AND (:startDate IS NULL OR e.date >= :startDate)
                                AND (:endDate IS NULL OR e.date <= :endDate)
                                AND (:minAmount IS NULL OR e.amount >= :minAmount)
                                AND (:maxAmount IS NULL OR e.amount <= :maxAmount)
                                AND (:description IS NULL OR LOWER(e.description) LIKE LOWER(CONCAT('%', :description, '%')))
                        """)
        Page<Expense> findByUserIdAndFilters(
                        @Param("userId") Long userId,
                        @Param("categoryId") Long categoryId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("minAmount") BigDecimal minAmount,
                        @Param("maxAmount") BigDecimal maxAmount,
                        @Param("description") String description,
                        Pageable pageable);
}
