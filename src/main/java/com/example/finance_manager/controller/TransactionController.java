package com.example.finance_manager.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

import com.example.finance_manager.dto.TransactionResponse;
import com.example.finance_manager.entity.Transaction;
import com.example.finance_manager.entity.User;
import com.example.finance_manager.security.CurrentUserService;
import com.example.finance_manager.service.TransactionService;

@RestController
public class TransactionController {

    private final TransactionService transactionService;
    private final CurrentUserService currentUserService;

    public TransactionController(
            TransactionService transactionService,
            CurrentUserService currentUserService) {

        this.transactionService = transactionService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/transactions")
    public TransactionResponse createTransaction(
            @Valid @RequestBody Transaction transaction) {

        User currentUser = currentUserService.getCurrentUser();

        transaction.setUser(currentUser);

        Transaction savedTransaction =
                transactionService.saveTransaction(transaction);

        return convertToTransactionResponse(savedTransaction);
    }

    @GetMapping("/transactions")
    public List<TransactionResponse> getAllTransactions() {

        User currentUser = currentUserService.getCurrentUser();

        return transactionService
                .getTransactionsByUserId(currentUser.getId())
                .stream()
                .map(this::convertToTransactionResponse)
                .toList();
    }

    @GetMapping("/transactions/{id}")
    public TransactionResponse getTransactionById(
            @PathVariable Long id) {

        Transaction transaction =
                transactionService.getTransactionById(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Transaction not found"));

        checkOwnership(transaction);

        return convertToTransactionResponse(transaction);
    }

    @GetMapping("/users/{userId}/transactions")
    public List<TransactionResponse> getTransactionsByUserId(
            @PathVariable Long userId) {

        User currentUser = currentUserService.getCurrentUser();

        if (!currentUser.getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot access another user's transactions");
        }

        return transactionService.getTransactionsByUserId(userId)
                .stream()
                .map(this::convertToTransactionResponse)
                .toList();
    }

    @DeleteMapping("/transactions/{id}")
    public void deleteTransaction(@PathVariable Long id) {

        Transaction transaction =
                transactionService.getTransactionById(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Transaction not found"));

        checkOwnership(transaction);

        transactionService.deleteTransaction(id);
    }

    @PutMapping("/transactions/{id}")
    public TransactionResponse updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody Transaction transaction) {

        Transaction existingTransaction =
                transactionService.getTransactionById(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Transaction not found"));

        checkOwnership(existingTransaction);

        transaction.setUser(existingTransaction.getUser());

        Transaction updatedTransaction =
                transactionService.updateTransaction(id, transaction);

        return convertToTransactionResponse(updatedTransaction);
    }

    private void checkOwnership(Transaction transaction) {

        User currentUser = currentUserService.getCurrentUser();

        if (!transaction.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot access another user's transaction");
        }
    }

    private TransactionResponse convertToTransactionResponse(
            Transaction transaction) {

        Long userId = null;

        if (transaction.getUser() != null) {
            userId = transaction.getUser().getId();
        }

        return new TransactionResponse(
                transaction.getId(),
                transaction.getTitle(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCategory(),
                transaction.getTransactionDate(),
                userId
        );
    }

}