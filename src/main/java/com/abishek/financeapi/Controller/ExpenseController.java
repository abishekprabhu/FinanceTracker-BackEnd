package com.abishek.financeapi.Controller;

import java.util.List;

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

import com.abishek.financeapi.DTO.ExpenseDTO;
import com.abishek.financeapi.DTO.TransactionDTO;
import com.abishek.financeapi.Enum.TransactionType;
import com.abishek.financeapi.Exception.CategoryNotFoundException;
import com.abishek.financeapi.Exception.ExpenseNotFoundException;
import com.abishek.financeapi.Exception.TransactionNotFoundException;
import com.abishek.financeapi.Exception.UserNotFoundException;
import com.abishek.financeapi.Service.Expense.ExpenseService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expense")
@CrossOrigin
@RequiredArgsConstructor
public class ExpenseController {
		
	private final ExpenseService expenseService;
	
	@PostMapping
	public ResponseEntity<ExpenseDTO> postExpenseEntity(@RequestBody ExpenseDTO dto){
		ExpenseDTO createdExpense = expenseService.createExpense(dto);
		
		if(createdExpense != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(createdExpense);			
		}else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	
	@GetMapping("/all")
	public ResponseEntity<List<ExpenseDTO>> getAllExpenses(){
		return ResponseEntity.ok(expenseService.getAllExpenses());		
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getExpenseById(@PathVariable Long id){
		try {
			return ResponseEntity.ok(expenseService.getExpenseById(id));
		}catch(EntityNotFoundException ex){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went Wrong"  + e.getMessage());
		}
	}
	

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAllExpenseByUserId(@PathVariable Long id) {
        try {
            List<ExpenseDTO> expenses = expenseService.getAllExpenseByUserId(id);
            return ResponseEntity.ok(expenses);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/user/{id}/category/{categoryId}")
    public ResponseEntity<?> getAllExpenseByCategory(@PathVariable Long id, @PathVariable Long categoryId) {
        try {
            List<ExpenseDTO> expenses = expenseService.getAllExpenseByUserIdAndCategoryId(id, categoryId);
            return ResponseEntity.ok(expenses);
        } catch (UserNotFoundException | CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateExpense(@PathVariable Long id , @RequestBody ExpenseDTO dto){
		try {
			return ResponseEntity.ok(expenseService.updateExpense(id, dto));
		}catch(ExpenseNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong ");
		}		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteExpense(@PathVariable Long id){
		try {
			expenseService.deleteExpense(id);
			return ResponseEntity.ok(null);
		}catch(ExpenseNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong ");
		}		
	}

}