package com.abishek.financeapi.Controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abishek.financeapi.DTO.BudgetDTO;
import com.abishek.financeapi.Exception.BudgetNotFoundException;
import com.abishek.financeapi.Model.Budget;
import com.abishek.financeapi.Service.Budget.BudgetService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/budgets")
@CrossOrigin("*")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping("/create")
    public ResponseEntity<Budget> createBudget(@RequestBody BudgetDTO budgetDTO) {
        Budget createdBudget = budgetService.createOrUpdateBudget(budgetDTO);
        if(createdBudget != null)
        	return ResponseEntity.status(HttpStatus.CREATED).body(createdBudget);
        else
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BudgetDTO>> getBudgetsByUserId(@PathVariable Long userId) {
        List<BudgetDTO> budgets = budgetService.getBudgetsByUserId(userId);
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("/user/{userId}/category/{categoryId}")
    public ResponseEntity<List<BudgetDTO>> getBudgetsByUserAndCategory(@PathVariable Long userId, @PathVariable Long categoryId) {
        List<BudgetDTO> budgets = budgetService.getBudgetsByUserAndCategory(userId, categoryId);
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("/monitor/{budgetId}")
    public ResponseEntity<BigDecimal> monitorBudget(@PathVariable Long budgetId) {
        BigDecimal totalSpent = budgetService.getTotalSpentForBudget(budgetId);
        return ResponseEntity.ok(totalSpent);
    }
    
	@GetMapping("/{id}")
	public ResponseEntity<?> getBudgetById(@PathVariable Long id){
		try {
			return ResponseEntity.ok(budgetService.getBudgetById(id));
		}catch(BudgetNotFoundException ex){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went Wrong"  + e.getMessage());
		}
	}
    
    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetDTO> updateBudget(
        @PathVariable Long budgetId, @RequestBody BudgetDTO budgetDTO) {
        
        BudgetDTO updatedBudget = budgetService.updateBudget(budgetId, budgetDTO);
        return ResponseEntity.ok(updatedBudget);
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long budgetId) {
        budgetService.deleteBudget(budgetId);
        return ResponseEntity.noContent().build();  // HTTP 204
    }
}

