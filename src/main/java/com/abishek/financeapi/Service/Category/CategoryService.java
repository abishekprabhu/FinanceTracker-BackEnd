package com.abishek.financeapi.Service.Category;

import java.util.List;

import com.abishek.financeapi.DTO.CategoryDTO;
import com.abishek.financeapi.Model.Category;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);

//    List<CategoryDTO> getAllCategories();

    List<CategoryDTO> getAllCategoryByUser(Long userId);

    CategoryDTO getCategoryById(Long id);

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long id);

    List<Category> getIncomeCategories(Long userId);

    List<Category> getExpenseCategories(Long userId);
}

