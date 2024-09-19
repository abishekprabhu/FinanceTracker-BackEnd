package com.abishek.financeapi.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BudgetDTO {
	
	private Long id;

    private Long userId;
    
    private Long categoryId; 
    
    private BigDecimal amount;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private BigDecimal totalSpent = BigDecimal.ZERO; 
    
    private boolean exceeded;

}

