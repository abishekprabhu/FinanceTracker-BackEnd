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
import com.abishek.financeapi.DTO.IncomeDTO;
import com.abishek.financeapi.Exception.CategoryNotFoundException;
import com.abishek.financeapi.Exception.IncomeNotFoundException;
import com.abishek.financeapi.Exception.UserNotFoundException;
import com.abishek.financeapi.Service.Income.IncomeService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/income")
@RequiredArgsConstructor
@CrossOrigin("*")
public class IncomeController {
	
	private final IncomeService incomeService;
	
	@PostMapping
	public ResponseEntity<IncomeDTO>  postIncome(@RequestBody IncomeDTO incomeDTO){
		IncomeDTO createdIncome = incomeService.createIncome(incomeDTO);
		
		if(createdIncome != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(createdIncome);
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<IncomeDTO>> getAllIncomes(){
		return ResponseEntity.ok(incomeService.getAllIncome());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getIncomeById(@PathVariable Long id){
		try {
			return ResponseEntity.ok(incomeService.getIncomeById(id));
		}catch(IncomeNotFoundException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
		}
	}
	
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAllIncomeByUserId(@PathVariable Long id) {
        try {
            List<IncomeDTO> incomes = incomeService.getAllIncomeByUserId(id);
            return ResponseEntity.ok(incomes);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/user/{id}/category/{categoryId}")
    public ResponseEntity<?> getAllIncomeByCategory(@PathVariable Long id, @PathVariable Long categoryId) {
        try {
            List<IncomeDTO> incomes = incomeService.getAllIncomeByUserIdAndCategoryId(id, categoryId);
            return ResponseEntity.ok(incomes);
        } catch (UserNotFoundException | CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateIncome(@PathVariable Long id ,@RequestBody IncomeDTO incomeDto){
		try {
			return ResponseEntity.ok(incomeService.updateIncome(id, incomeDto));
		}catch(IncomeNotFoundException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteIncome(@PathVariable Long id ){
		try {
			incomeService.deleteIncome(id);
			return ResponseEntity.ok(null);
		}catch(IncomeNotFoundException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
		}
	}
	
	
	
}
