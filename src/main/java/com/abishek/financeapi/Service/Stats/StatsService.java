package com.abishek.financeapi.Service.Stats;

import com.abishek.financeapi.DTO.GraphDTO;
import com.abishek.financeapi.DTO.StatsDTO;

public interface StatsService {
	
	GraphDTO getChartData();
	
	StatsDTO getStats();
}
