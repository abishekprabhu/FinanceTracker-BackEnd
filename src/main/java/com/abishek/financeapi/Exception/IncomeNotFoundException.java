package com.abishek.financeapi.Exception;

public class IncomeNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public IncomeNotFoundException(String message) {
		super(message);
	}
}
