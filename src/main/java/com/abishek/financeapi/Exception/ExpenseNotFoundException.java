package com.abishek.financeapi.Exception;

public class ExpenseNotFoundException extends RuntimeException{

	public ExpenseNotFoundException(String message) {
		super(message);
	}
}
