package com.abishek.financeapi.Service.Category;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abishek.financeapi.DTO.CategoryDTO;
import com.abishek.financeapi.Exception.CategoryNotFoundException;
import com.abishek.financeapi.Exception.CustomException;
import com.abishek.financeapi.Exception.DuplicateCategoryException;
import com.abishek.financeapi.Model.Category;
import com.abishek.financeapi.Model.Expense;
import com.abishek.financeapi.Model.Income;
import com.abishek.financeapi.Repository.CategoryRepository;
import com.abishek.financeapi.Repository.ExpenseRepository;
import com.abishek.financeapi.Repository.IncomeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
    	Optional<Category> existingCategory = categoryRepository.findByName(categoryDTO.getName());
    	
    	if(existingCategory.isPresent()) {
    		throw new DuplicateCategoryException("Category with name " + categoryDTO.getName() +" already Exists");
    	}
        Category category = new Category();
        category.setName(categoryDTO.getName());
        Category savedCategory = categoryRepository.save(category);
        return mapToDTO(savedCategory);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        return mapToDTO(category);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
    	
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    	
    	Optional<Category> existingCategory = categoryRepository.findByName(categoryDTO.getName());
    	
    	if(existingCategory.isPresent()) {
    		throw new DuplicateCategoryException("Category with name " + categoryDTO.getName() +" already Exists");
    	}
    	
        category.setName(categoryDTO.getName());
        Category updatedCategory = categoryRepository.save(category);
        return mapToDTO(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        // Set category to null for all associated Income and Expense records
        List<Income> incomes = incomeRepository.findByCategory(category);
        for (Income income : incomes) {
            income.setCategory(null); // default category if required
            incomeRepository.save(income);
        }

        List<Expense> expenses = expenseRepository.findByCategory(category);
        for (Expense expense : expenses) {
            expense.setCategory(null); // default category if required
            expenseRepository.save(expense);
        }

        // Delete the category
        categoryRepository.deleteById(id);
    }

    private CategoryDTO mapToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }
}
