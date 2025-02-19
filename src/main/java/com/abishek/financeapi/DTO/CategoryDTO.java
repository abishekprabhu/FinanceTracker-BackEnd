package com.abishek.financeapi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryDTO {
	
    private Long id;
    
    private String name;

    private Long userId;
//
//    private List<IncomeDTO> incomes;
//    private List<ExpenseDTO> expenses;
}
