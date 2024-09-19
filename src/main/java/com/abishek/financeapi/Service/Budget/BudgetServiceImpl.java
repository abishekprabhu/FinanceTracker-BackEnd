package com.abishek.financeapi.Service.Budget;


import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.abishek.financeapi.DTO.BudgetDTO;
import com.abishek.financeapi.Exception.BudgetNotFoundException;
import com.abishek.financeapi.Exception.CategoryNotFoundException;
import com.abishek.financeapi.Exception.UserNotFoundException;
import com.abishek.financeapi.Model.Budget;
import com.abishek.financeapi.Model.Category;
import com.abishek.financeapi.Model.User;
import com.abishek.financeapi.Repository.BudgetRepository;
import com.abishek.financeapi.Repository.CategoryRepository;
import com.abishek.financeapi.Repository.ExpenseRepository;
import com.abishek.financeapi.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
	
	
    private final BudgetRepository budgetRepository;

    private final ExpenseRepository expenseRepository; // To calculate total expenses

    private final UserRepository userRepository;
    
    private final CategoryRepository categoryRepository;
    
    @Override
    public Budget createOrUpdateBudget(BudgetDTO budgetDTO) {
        User user = userRepository.findById(budgetDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Budget budget = new Budget();
        budget.setUser(user);
        if (budgetDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(budgetDTO.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
            budget.setCategory(category);
        }
        budget.setAmount(budgetDTO.getAmount());
        budget.setStartDate(budgetDTO.getStartDate());
        budget.setEndDate(budgetDTO.getEndDate());

        return budgetRepository.save(budget);
    }
    
    @Override
    public List<BudgetDTO> getBudgetsByUserId(Long userId) {
    	List<BudgetDTO> UserBudget =  budgetRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
//    	 log.info("info budget"+ UserBudget);
    	return UserBudget;
       
    }

    @Override
    public List<BudgetDTO> getBudgetsByUserAndCategory(Long userId, Long categoryId) {
        List<Budget> budgets = budgetRepository.findByUserIdAndCategoryId(userId, categoryId);
        return budgets.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalSpentForBudget(Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new BudgetNotFoundException("Budget not found"));

        // Calculate total expenses for the budget
        BigDecimal totalSpent = expenseRepository.calculateTotalExpensesForBudget(
                budget.getUser().getId(), 
                budget.getStartDate(), 
                budget.getEndDate(), 
                budget.getCategory().getId());

        return totalSpent;
    }
    
    @Override
    public BudgetDTO getBudgetById(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException("Budget not found"));        
        return mapToDTO(budget);
    }
    
    @Override
    public BudgetDTO updateBudget(Long budgetId, BudgetDTO budgetDTO) {
        Budget existingBudget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new BudgetNotFoundException("Budget with ID " + budgetId + " not found"));

        
        if (budgetDTO.getAmount() != null) {
            existingBudget.setAmount(budgetDTO.getAmount());
        }
        if (budgetDTO.getStartDate() != null) {
            existingBudget.setStartDate(budgetDTO.getStartDate());
        }
        if (budgetDTO.getEndDate() != null) {
            existingBudget.setEndDate(budgetDTO.getEndDate());
        }
        if (budgetDTO.getUserId() != null) {
        	User user = userRepository.findById(budgetDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        	existingBudget.setUser(user);
        }
        if (budgetDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(budgetDTO.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
            existingBudget.setCategory(category);
        }

        Budget updatedBudget = budgetRepository.save(existingBudget);
        return mapToDTO(updatedBudget);
    }

    
    @Override
    public void deleteBudget(Long budgetId) {
        Budget existingBudget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new BudgetNotFoundException("Budget with ID " + budgetId + " not found"));

        budgetRepository.delete(existingBudget);
    }

    private BudgetDTO mapToDTO(Budget budget) {
        BudgetDTO budgetDTO = new BudgetDTO();
        budgetDTO.setId(budget.getId());
        budgetDTO.setUserId(budget.getUser().getId());
        budgetDTO.setCategoryId(budget.getCategory().getId());
        budgetDTO.setAmount(budget.getAmount());
        budgetDTO.setStartDate(budget.getStartDate());
        budgetDTO.setEndDate(budget.getEndDate());
        budgetDTO.setTotalSpent(getTotalSpentForBudget(budget.getId()));  // Calculate total spent dynamically
        budgetDTO.setExceeded(budgetDTO.getTotalSpent().compareTo(budgetDTO.getAmount()) > 0);  // Determine if exceeded
        return budgetDTO;
    }
    
    
    


}
