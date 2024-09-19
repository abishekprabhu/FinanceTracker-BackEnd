package com.abishek.financeapi.Service.Stats;

import com.abishek.financeapi.DTO.GraphDTO;
import com.abishek.financeapi.DTO.PdfDTO;
import com.abishek.financeapi.DTO.StatsDTO;

public interface StatsService {
	
	GraphDTO getChartDataMonthly();
	
	StatsDTO getStats();

	GraphDTO getChartDataWeekly();

	GraphDTO getChartDataQuartarly();

	GraphDTO getChartDataYearly();

	PdfDTO getTransactionDataMonthly();
		
}
