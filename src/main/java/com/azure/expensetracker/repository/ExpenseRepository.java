package com.azure.expensetracker.repository;

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

        @Query("""
                        SELECT e
                        FROM Expense e
                        JOIN FETCH e.category
                        WHERE e.id = :id
                        AND e.deletedAt IS NULL
                        """)
        Optional<Expense> findByIdWithCategory(@Param("id") Long id);

        @Query("""
                        SELECT e
                        FROM Expense e
                        JOIN FETCH e.category
                        WHERE e.deletedAt IS NULL
                        """)
        List<Expense> findAllWithCategory();

        @EntityGraph(attributePaths = { "category" })
        Page<Expense> findByUser_IdAndDeletedAtIsNull(Long userId, Pageable pageable);

        Optional<Expense> findByIdAndDeletedAtIsNull(Long id);

}
