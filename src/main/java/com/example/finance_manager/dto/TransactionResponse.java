package com.example.finance_manager.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.finance_manager.entity.TransactionType;

public class TransactionResponse {

    private Long id;

    private String title;

    private BigDecimal amount;

    private TransactionType type;

    private String category;

    private LocalDate transactionDate;

    private Long userId;

    public TransactionResponse() {

    }

    public TransactionResponse(
            Long id,
            String title,
            BigDecimal amount,
            TransactionType type,
            String category,
            LocalDate transactionDate,
            Long userId) {

        this.id = id;
        this.title = title;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.transactionDate = transactionDate;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}