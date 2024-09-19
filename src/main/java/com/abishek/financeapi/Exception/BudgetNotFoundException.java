package com.abishek.financeapi.Exception;

public class BudgetNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public BudgetNotFoundException(String message) {
		super(message);
	}
}
