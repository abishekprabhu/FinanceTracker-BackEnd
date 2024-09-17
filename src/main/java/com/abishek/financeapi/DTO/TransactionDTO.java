package com.abishek.financeapi.DTO;

import java.time.LocalDate;

import com.abishek.financeapi.Enum.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionDTO {
	
    private Long id;
    
    private String description;
    
    private double amount;
    
    private TransactionType type; // INCOME or EXPENSE
    
    private LocalDate date;
    
    private Long userId;
    
    private Long categoryId;
    
    private Long incomeId;
    
    private Long expenseId;
}
