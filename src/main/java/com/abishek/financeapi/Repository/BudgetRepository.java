package com.abishek.financeapi.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abishek.financeapi.Model.Budget;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUserId(Long userId);
    List<Budget> findByUserIdAndCategoryId(Long userId, Long categoryId);
    Optional<Budget> findByUserIdAndStartDateAndEndDate(Long userId, LocalDate startDate, LocalDate endDate);
}
