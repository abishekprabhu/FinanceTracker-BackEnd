package com.abishek.financeapi.Service.Income;

import java.util.List;

import com.abishek.financeapi.DTO.IncomeDTO;
import com.abishek.financeapi.Model.Income;

public interface IncomeService {

//	Income postIncome(IncomeDTO incomeDTO);
    IncomeDTO createIncome(IncomeDTO incomeDTO);
	
	List<IncomeDTO> getAllIncome();
	
	List<IncomeDTO> getAllIncomeByDescending();
	
	IncomeDTO getIncomeById(Long id);
	
	IncomeDTO updateIncome(Long id , IncomeDTO incomeDTO);
	
	void deleteIncome(Long id );

	List<IncomeDTO> getAllIncomeByUserId(Long userId);

	List<IncomeDTO> getAllIncomeByUserIdAndCategoryId(Long userId, Long categoryId);
}