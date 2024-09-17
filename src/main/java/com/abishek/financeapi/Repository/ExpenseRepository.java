package com.abishek.financeapi.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.abishek.financeapi.Model.Category;
import com.abishek.financeapi.Model.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	
	List<Expense> findByDateBetween(LocalDate startDate , LocalDate endDate);
	
	@Query("SELECT SUM(e.amount) FROM Expense e")
	Double SumAllAmount();
	
	Optional<Expense> findFirstByOrderByDateDesc();
	
	 List<Expense> findByCategory(Category category);
	 
	 List<Expense> findByUserId(Long userId) ;

	List<Expense> findByUserIdAndCategoryId(Long userId, Long categoryId);
}
