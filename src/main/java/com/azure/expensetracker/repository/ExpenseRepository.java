package com.azure.expensetracker.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.azure.expensetracker.dto.response.ExpenseSummaryResponse;
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

        @Query("""
                        SELECT new com.azure.expensetracker.dto.response.StatisticsEsponseSummaryResponse(
                        COALESCE(SUM(e.amount), 0),
                        COALESCE(COUNT(e.id), 0),
                        COALESCE(AVG(e.amount), 0.0)
                        )
                        FROM Expense e
                        WHERE e.user.id = :userId
                                AND e.expenseDate >= :startDate
                                AND e.expenseDate <= :endDate
                                AND e.deletedAt IS NULL
                        """)
        ExpenseSummaryResponse getExpenseSummary(
                        @Param("userId") Long userId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

}
