package com.abishek.financeapi.DTO;

import java.util.List;

import com.abishek.financeapi.Model.Expense;
import com.abishek.financeapi.Model.Income;

import lombok.Data;

@Data
public class GraphDTO {
	
	private List<Expense> expenseList;
	
	private List<Income> incomeList;
}

