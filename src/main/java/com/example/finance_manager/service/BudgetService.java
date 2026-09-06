package com.example.finance_manager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.finance_manager.entity.Budget;
import com.example.finance_manager.repository.BudgetRepository;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public Budget saveBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Optional<Budget> getBudgetById(Long id) {
        return budgetRepository.findById(id);
    }

    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }

    public Budget updateBudget(Long id, Budget updatedBudget) {
        Budget existingBudget = budgetRepository.findById(id).orElseThrow();

        existingBudget.setCategory(updatedBudget.getCategory());
        existingBudget.setAmount(updatedBudget.getAmount());
        existingBudget.setMonth(updatedBudget.getMonth());
        existingBudget.setUser(updatedBudget.getUser());

        return budgetRepository.save(existingBudget);
    }

    public List<Budget> getBudgetsByUserId(Long userId) {
        return budgetRepository.findByUserId(userId);
    }

}