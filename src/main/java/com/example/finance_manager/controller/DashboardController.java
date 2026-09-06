package com.example.finance_manager.controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.finance_manager.dto.DashboardResponse;
import com.example.finance_manager.entity.TransactionType;
import com.example.finance_manager.entity.User;
import com.example.finance_manager.security.CurrentUserService;
import com.example.finance_manager.service.TransactionService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final TransactionService transactionService;
    private final CurrentUserService currentUserService;

    public DashboardController(
            TransactionService transactionService,
            CurrentUserService currentUserService) {

        this.transactionService = transactionService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/income/{userId}")
    public BigDecimal getTotalIncome(@PathVariable Long userId) {

        checkUserAccess(userId);

        return transactionService.getTotalAmountByUserIdAndType(
                userId,
                TransactionType.INCOME);
    }

    @GetMapping("/expense/{userId}")
    public BigDecimal getTotalExpense(@PathVariable Long userId) {

        checkUserAccess(userId);

        return transactionService.getTotalAmountByUserIdAndType(
                userId,
                TransactionType.EXPENSE);
    }

    @GetMapping("/balance/{userId}")
    public BigDecimal getBalance(@PathVariable Long userId) {

        checkUserAccess(userId);

        BigDecimal income =
                transactionService.getTotalAmountByUserIdAndType(
                        userId,
                        TransactionType.INCOME);

        BigDecimal expense =
                transactionService.getTotalAmountByUserIdAndType(
                        userId,
                        TransactionType.EXPENSE);

        return income.subtract(expense);
    }

    @GetMapping("/{userId}")
    public DashboardResponse getDashboard(
            @PathVariable Long userId) {

        checkUserAccess(userId);

        BigDecimal totalIncome =
                transactionService.getTotalAmountByUserIdAndType(
                        userId,
                        TransactionType.INCOME);

        BigDecimal totalExpense =
                transactionService.getTotalAmountByUserIdAndType(
                        userId,
                        TransactionType.EXPENSE);

        BigDecimal balance =
                totalIncome.subtract(totalExpense);

        return new DashboardResponse(
                totalIncome,
                totalExpense,
                balance
        );
    }

    private void checkUserAccess(Long userId) {

        User currentUser = currentUserService.getCurrentUser();

        if (!currentUser.getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot access another user's dashboard");
        }
    }

}