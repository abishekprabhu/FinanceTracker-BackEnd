package com.abishek.financeapi.Service.Stats;

import java.util.Map;

import com.abishek.financeapi.DTO.GraphDTO;
import com.abishek.financeapi.DTO.MonthlyDataDTO;
import com.abishek.financeapi.DTO.PdfDTO;
import com.abishek.financeapi.DTO.PercentageDTO;
import com.abishek.financeapi.DTO.StatsDTO;

public interface StatsService {
	
	GraphDTO getChartDataMonthly(Long userId);
	
//	StatsDTO getStats();

	GraphDTO getChartDataWeekly();

	GraphDTO getChartDataQuartarly();

	GraphDTO getChartDataYearly();

	PdfDTO getTransactionDataMonthly();

	StatsDTO getUserStats(Long userId);

	PercentageDTO getTransactionSummary(Long userId);

	MonthlyDataDTO getMonthlyData(Long userId);
		
}
