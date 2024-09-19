package com.abishek.financeapi.DTO;

import java.util.List;

import com.abishek.financeapi.Model.Transaction;

import lombok.Data;


@Data
public class PdfDTO {

	private List<Transaction> transactionList;
}
