package com.abishek.financeapi.Service.Transaction;

import java.util.List;

import com.abishek.financeapi.DTO.TransactionDTO;
import com.abishek.financeapi.Enum.TransactionType;

public interface TransactionService {

    TransactionDTO createTransaction(TransactionDTO transactionDTO);

    List<TransactionDTO> getAllTransactions();

    TransactionDTO getTransactionById(Long id);

    TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO);

    void deleteTransaction(Long id);
    
    List<TransactionDTO> getAllTransactions(Long userId);

    List<TransactionDTO> getTransactionsByCategory(Long userId, Long categoryId);

    List<TransactionDTO> getTransactionsByType(Long userId, TransactionType type);
}
