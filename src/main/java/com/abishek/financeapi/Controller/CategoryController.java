package com.abishek.financeapi.Controller;

import java.util.List;

import com.abishek.financeapi.DTO.CategoryDTO;
import com.abishek.financeapi.Service.Category.CategoryService;
import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.CachePut;
//import org.springframework.cache.annotation.Cacheable;
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

import com.abishek.financeapi.Model.Category;


@RestController
@RequestMapping("/api/categories")
@CrossOrigin()
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
 //   @CacheEvict(value = "category", allEntries = true)
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        if(createdCategory != null)
        	return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        else
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/all/user/{userId}")
//    @Cacheable(value = "category", key = "#userId", unless = "#result == null || #result.EMPTY")
    public ResponseEntity<List<CategoryDTO>> getAllCategories(@PathVariable Long userId) {
        List<CategoryDTO> categories = categoryService.getAllCategoryByUser(userId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
  //  @Cacheable(value = "category", key = "#id", unless = "#result == null")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryDTO);
    }

    @PutMapping("/{id}")
  //  @CachePut(value = "category", key = "#id", unless = "#result == null")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        if(updatedCategory != null)
        	return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedCategory);
        else
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/{id}")
   // @CacheEvict(value = "category", key = "#id")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/income/user/{userId}")
   // @Cacheable(value = "category", key = "#userId", unless = "#result == null || #result.EMPTY")
    public ResponseEntity<List<Category>> getIncomeCategories(@PathVariable Long userId) {
        List<Category> incomeCategories = categoryService.getIncomeCategories(userId);
        return ResponseEntity.ok(incomeCategories);
    }

    @GetMapping("/expense/user/{userId}")
    //@Cacheable(value = "category", key = "#userId", unless = "#result == null || #result.EMPTY")
    public ResponseEntity<List<Category>> getExpenseCategories(@PathVariable Long userId) {
        List<Category> expenseCategories = categoryService.getExpenseCategories(userId);
        return ResponseEntity.ok(expenseCategories);
    }
    
}

