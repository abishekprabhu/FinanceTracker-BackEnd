package com.abishek.financeapi.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.abishek.financeapi.Model.Category;
import com.abishek.financeapi.Model.Income;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
	
	List<Income> findByDateBetween(LocalDate startDate , LocalDate endDate);
	
//	@Query("SELECT SUM(i.amount) FROM Income i")
//	Double SumAllAmount();
	
	Optional<Income> findFirstByOrderByDateDesc();
	
	List<Income> findByCategory(Category category);
	 
	List<Income> findByUserId(Long userId) ;

	List<Income> findByUserIdAndCategoryId(Long userId, Long categoryId);

	List<Income> findByDateBetweenOrderByDateDesc(LocalDate startDate, LocalDate endDate);

	List<Income> findByDateBetweenOrderByDateAsc(LocalDate startDate, LocalDate endDate);

	
	List<Income> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
	 
    // Query to calculate the total income for a specific user
    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.user.id = :userId")
    Double findTotalByUserId(Long userId);

	@Query("SELECT SUM(i.amount) FROM Income i WHERE i.user.id = :userId")
	Double SumAllAmountByUserId(Long userId);

	Optional<Income> findFirstByUserIdOrderByDateDesc(Long userId);

	List<Income> findAllByUserId(Long userId);

	List<Income> findByUserIdAndDateBetweenOrderByDateAsc(Long userId, LocalDate startDate, LocalDate endDate);
}
