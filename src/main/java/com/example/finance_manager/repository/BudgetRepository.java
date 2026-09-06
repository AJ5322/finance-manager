package com.example.finance_manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.finance_manager.entity.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUserId(Long userId);

}