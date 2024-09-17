package com.abishek.financeapi.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abishek.financeapi.DTO.GraphDTO;
import com.abishek.financeapi.Service.Stats.StatsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stats")
@CrossOrigin("*")
public class StatsController {

	private final StatsService statsService;
	
	@GetMapping("/chart")
	public ResponseEntity<GraphDTO> getChartDetails(){
		return ResponseEntity.ok(statsService.getChartData());
	}
	
	@GetMapping
	public ResponseEntity<?> getStats(){
		return ResponseEntity.ok(statsService.getStats());
	}
	
	
}
