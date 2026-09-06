package com.example.finance_manager.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.finance_manager.entity.Transaction;
import com.example.finance_manager.entity.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.type = :type")
    BigDecimal findTotalAmountByUserIdAndType(
            @Param("userId") Long userId,
            @Param("type") TransactionType type);

}