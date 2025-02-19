package com.abishek.financeapi.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.abishek.financeapi.Model.Category;
import com.abishek.financeapi.Model.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	
	List<Expense> findByDateBetween(LocalDate startDate , LocalDate endDate);
	
//	@Query("SELECT SUM(e.amount) FROM Expense e")
//	Double SumAllAmount();
	
	Optional<Expense> findFirstByOrderByDateDesc();
	
	List<Expense> findByCategory(Category category);
	 
	List<Expense> findByUserId(Long userId) ;

	List<Expense> findByUserIdAndCategoryId(Long userId, Long categoryId);
	
	// COALESCE function ensures that if there are no expenses (i.e., the sum is null), it returns 0.
	@Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user.id = :userId AND e.date BETWEEN :startDate AND :endDate AND e.category.id = :categoryId")
	BigDecimal calculateTotalExpensesForBudget(@Param("userId") Long userId, 
	                                           @Param("startDate") LocalDate startDate, 
	                                           @Param("endDate") LocalDate endDate, 
	                                           @Param("categoryId") Long categoryId);

	List<Expense> findByDateBetweenOrderByDateDesc(LocalDate startDate, LocalDate endDate);

	List<Expense> findByDateBetweenOrderByDateAsc(LocalDate startDate, LocalDate endDate);

	List<Expense> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
	
    // Query to calculate the total expense for a specific user
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId")
    Double findTotalByUserId(Long userId);

	@Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId")
    Double SumAllAmountByUserId(Long userId);

	Optional<Expense> findFirstByUserIdOrderByDateDesc(Long userId);

	List<Expense> findAllByUserId(Long userId);

    List<Expense> findByUserIdAndDateBetweenOrderByDateAsc(Long userId, LocalDate startDate, LocalDate endDate);
}
