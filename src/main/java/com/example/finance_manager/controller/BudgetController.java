package com.example.finance_manager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.finance_manager.dto.BudgetResponse;
import com.example.finance_manager.entity.Budget;
import com.example.finance_manager.entity.User;
import com.example.finance_manager.security.CurrentUserService;
import com.example.finance_manager.service.BudgetService;
import jakarta.validation.Valid;


@RestController
public class BudgetController {

    private final BudgetService budgetService;
    private final CurrentUserService currentUserService;

    public BudgetController(
            BudgetService budgetService,
            CurrentUserService currentUserService) {

        this.budgetService = budgetService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/budgets")
    public BudgetResponse createBudget(
            @RequestBody Budget budget) {

        User currentUser = currentUserService.getCurrentUser();

        budget.setUser(currentUser);

        Budget savedBudget = budgetService.saveBudget(budget);

        return convertToBudgetResponse(savedBudget);
    }

    @GetMapping("/budgets")
    public List<BudgetResponse> getAllBudgets() {

        User currentUser = currentUserService.getCurrentUser();

        return budgetService
                .getBudgetsByUserId(currentUser.getId())
                .stream()
                .map(this::convertToBudgetResponse)
                .toList();
    }

    @GetMapping("/budgets/{id}")
    public BudgetResponse getBudgetById(
            @PathVariable Long id) {

        Budget budget = budgetService.getBudgetById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Budget not found"));

        checkOwnership(budget);

        return convertToBudgetResponse(budget);
    }

    @GetMapping("/users/{userId}/budgets")
    public List<BudgetResponse> getBudgetsByUserId(
            @PathVariable Long userId) {

        User currentUser = currentUserService.getCurrentUser();

        if (!currentUser.getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot access another user's budgets");
        }

        return budgetService.getBudgetsByUserId(userId)
                .stream()
                .map(this::convertToBudgetResponse)
                .toList();
    }

    @DeleteMapping("/budgets/{id}")
    public void deleteBudget(@PathVariable Long id) {

        Budget budget = budgetService.getBudgetById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Budget not found"));

        checkOwnership(budget);

        budgetService.deleteBudget(id);
    }

    @PutMapping("/budgets/{id}")
    public BudgetResponse updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody Budget budget) {

        Budget existingBudget = budgetService.getBudgetById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Budget not found"));

        checkOwnership(existingBudget);

        budget.setUser(existingBudget.getUser());

        Budget updatedBudget =
                budgetService.updateBudget(id, budget);

        return convertToBudgetResponse(updatedBudget);
    }

    private void checkOwnership(Budget budget) {

        User currentUser = currentUserService.getCurrentUser();

        if (!budget.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot access another user's budget");
        }
    }

    private BudgetResponse convertToBudgetResponse(
            Budget budget) {

        Long userId = null;

        if (budget.getUser() != null) {
            userId = budget.getUser().getId();
        }

        return new BudgetResponse(
                budget.getId(),
                budget.getCategory(),
                budget.getAmount(),
                budget.getMonth(),
                userId
        );
    }

    
}