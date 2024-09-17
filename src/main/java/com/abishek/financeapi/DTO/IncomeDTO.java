package com.abishek.financeapi.DTO;

import java.time.LocalDate;

//import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IncomeDTO {
	
	private Long  id;
	
//	private String title;

	private String description;
	
	private double amount;
	
	private LocalDate date;
	
//	private Integer amount;

	
    private Long userId;
    
	private Long categoryId;
}