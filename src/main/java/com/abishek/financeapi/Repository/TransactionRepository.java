package com.abishek.financeapi.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abishek.financeapi.Enum.TransactionType;
import com.abishek.financeapi.Model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
    List<Transaction> findByUserId(Long userId);
    
    Optional<Transaction> findByExpenseId(Long expenseId);

    List<Transaction> findByUserIdAndCategoryId(Long userId, Long categoryId);

    List<Transaction> findByUserIdAndType(Long userId, TransactionType type);

	Optional<Transaction> findByIncomeId(Long incomeId);
}
