package com.abishek.financeapi.DTO;


import com.abishek.financeapi.Model.Expense;
import com.abishek.financeapi.Model.Income;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatsDTO {
	
	private Double income;
	
	private Double expense;
	
	private Income latestIncome;
	
	private Expense latestExpense;
	
	private Double balance;
	
	private Double minIncome;
	
	private Double maxIncome;
	
	private Double minExpense;
	
	private Double maxExpense;
}