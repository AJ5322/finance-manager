package com.example.finance_manager.dto;

import java.math.BigDecimal;

public class BudgetResponse {

    private Long id;

    private String category;

    private BigDecimal amount;

    private String month;

    private Long userId;

    public BudgetResponse() {
    }

    public BudgetResponse(
            Long id,
            String category,
            BigDecimal amount,
            String month,
            Long userId) {

        this.id = id;
        this.category = category;
        this.amount = amount;
        this.month = month;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}