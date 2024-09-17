package com.abishek.financeapi.Service.Expense;

import java.util.List;

import com.abishek.financeapi.DTO.ExpenseDTO;
import com.abishek.financeapi.Model.Expense;

public interface ExpenseService {

	ExpenseDTO createExpense(ExpenseDTO expenseDTO);
	
	List<ExpenseDTO> getAllExpenses();
	
	List<ExpenseDTO> getAllExpensesByDescending();
	
	ExpenseDTO getExpenseById(Long id);
	
	ExpenseDTO updateExpense(Long id , ExpenseDTO expenseDTO);
	
	void deleteExpense(Long id );

	List<ExpenseDTO> getAllExpenseByUserId(Long userId);

	List<ExpenseDTO> getAllExpenseByUserIdAndCategoryId(Long userId, Long categoryId);
	
}