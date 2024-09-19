package com.abishek.financeapi.Service.Budget;

import java.math.BigDecimal;
import java.util.List;

import com.abishek.financeapi.DTO.BudgetDTO;
import com.abishek.financeapi.Model.Budget;

public interface BudgetService {
	
	 Budget createOrUpdateBudget(BudgetDTO budgetDTO);
	 
	List<BudgetDTO> getBudgetsByUserId(Long userId);
	
	BigDecimal getTotalSpentForBudget(Long budgetId);

	List<BudgetDTO> getBudgetsByUserAndCategory(Long userId, Long categoryId);

	void deleteBudget(Long budgetId);

	BudgetDTO updateBudget(Long budgetId, BudgetDTO budgetDTO);

	BudgetDTO getBudgetById(Long id);
}
