package com.azure.expensetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.azure.expensetracker.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
