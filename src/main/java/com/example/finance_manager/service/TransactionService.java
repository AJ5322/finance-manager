package com.example.finance_manager.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.finance_manager.entity.Transaction;
import com.example.finance_manager.entity.TransactionType;
import com.example.finance_manager.repository.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    public Transaction updateTransaction(
            Long id,
            Transaction updatedTransaction) {

        Transaction existingTransaction =
                transactionRepository.findById(id).orElseThrow();

        existingTransaction.setTitle(updatedTransaction.getTitle());
        existingTransaction.setAmount(updatedTransaction.getAmount());
        existingTransaction.setType(updatedTransaction.getType());
        existingTransaction.setCategory(updatedTransaction.getCategory());
        existingTransaction.setTransactionDate(updatedTransaction.getTransactionDate());
        existingTransaction.setUser(updatedTransaction.getUser());

        return transactionRepository.save(existingTransaction);
    }

    public List<Transaction> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    public BigDecimal getTotalAmountByUserIdAndType(
            Long userId,
            TransactionType type) {

        return transactionRepository.findTotalAmountByUserIdAndType(userId, type);
    }

}