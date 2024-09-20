package com.abishek.financeapi.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abishek.financeapi.DTO.GraphDTO;
import com.abishek.financeapi.DTO.MonthlyDataDTO;
import com.abishek.financeapi.DTO.PdfDTO;
import com.abishek.financeapi.DTO.PercentageDTO;
import com.abishek.financeapi.Service.Stats.StatsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stats")
@CrossOrigin("*")
public class StatsController {

	private final StatsService statsService;
	
	@GetMapping("/chart/weekly")
	public ResponseEntity<GraphDTO> getChartWeekly(){
		return ResponseEntity.ok(statsService.getChartDataWeekly());
	}
	@GetMapping("/chart/monthly")
	public ResponseEntity<GraphDTO> getChartMonthly(){
		return ResponseEntity.ok(statsService.getChartDataMonthly());
	}
	
	@GetMapping("/chart/quartarly")
	public ResponseEntity<GraphDTO> getChartQuartarly(){
		return ResponseEntity.ok(statsService.getChartDataQuartarly());
	}
	
	@GetMapping("/chart/yearly")
	public ResponseEntity<GraphDTO> getChartYearly(){
		return ResponseEntity.ok(statsService.getChartDataYearly());
	}
	
	
	@GetMapping("/pdf/monthly")
	public ResponseEntity<PdfDTO> getPdfMonthly(){
		return ResponseEntity.ok(statsService.getTransactionDataMonthly());
	}
	
	@GetMapping
	public ResponseEntity<?> getStats(){
		return ResponseEntity.ok(statsService.getStats());
	}
	
    @GetMapping("/summary/{userId}")
    public ResponseEntity<PercentageDTO> getTransactionSummary(@PathVariable Long userId) {
        PercentageDTO summary = statsService.getTransactionSummary(userId);
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/monthly-data")
    public ResponseEntity<MonthlyDataDTO> getMonthlyData() {
        MonthlyDataDTO monthlyData = statsService.getMonthlyData();
        return ResponseEntity.ok(monthlyData);
    }

	
	
}
