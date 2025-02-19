package com.abishek.financeapi.Controller;

import com.abishek.financeapi.DTO.CategoryDTO;
import com.abishek.financeapi.Service.Category.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CategoryControllerTest {

    // Retrieves all categories for a valid user ID
    @Test
    public void test_get_all_categories_valid_user_id() {
        Long userId = 1L;
        List<CategoryDTO> expectedCategories = List.of(new CategoryDTO(1L, "Category1", userId), new CategoryDTO(2L, "Category2", userId));

        CategoryService categoryService = mock(CategoryService.class);
        when(categoryService.getAllCategoryByUser(userId)).thenReturn(expectedCategories);

        CategoryController categoryController = new CategoryController(categoryService);
        ResponseEntity<List<CategoryDTO>> response = categoryController.getAllCategories(userId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expectedCategories, response.getBody());
    }

    // Successfully creates a category with valid data
    @Test
    public void test_create_category_with_valid_data() {
        CategoryService categoryService = mock(CategoryService.class);
        CategoryController categoryController = new CategoryController(categoryService);
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Groceries", 1L);
        when(categoryService.createCategory(categoryDTO)).thenReturn(categoryDTO);

        ResponseEntity<CategoryDTO> response = categoryController.createCategory(categoryDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Groceries", response.getBody().getName());
    }

    // User ID does not exist, returns an empty list
    @Test
    public void test_get_all_categories_non_existent_user_id() {
        Long userId = 999L;
        List<CategoryDTO> expectedCategories = List.of();

        CategoryService categoryService = mock(CategoryService.class);
        when(categoryService.getAllCategoryByUser(userId)).thenReturn(expectedCategories);

        CategoryController categoryController = new CategoryController(categoryService);
        ResponseEntity<List<CategoryDTO>> response = categoryController.getAllCategories(userId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().isEmpty());
    }

    // Handles null input for categoryDTO gracefully
    @Test
    public void test_create_category_with_null_input() {
        CategoryService categoryService = mock(CategoryService.class);
        CategoryController categoryController = new CategoryController(categoryService);
        when(categoryService.createCategory(null)).thenReturn(null);

        ResponseEntity<CategoryDTO> response = categoryController.createCategory(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Successfully update a category when valid ID and data are provided
    @Test
    public void test_update_category_success() {
        CategoryService categoryService = mock(CategoryService.class);
        CategoryController categoryController = new CategoryController(categoryService);
        Long validId = 1L;
        CategoryDTO categoryDTO = new CategoryDTO(validId, "Updated Category", 1L);
        when(categoryService.updateCategory(validId, categoryDTO)).thenReturn(categoryDTO);

        ResponseEntity<CategoryDTO> response = categoryController.updateCategory(validId, categoryDTO);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(categoryDTO, response.getBody());
    }

    // Handle non-existent ID gracefully by returning HTTP 400 Bad Request
    @Test
    public void test_update_category_non_existent_id() {
        CategoryService categoryService = mock(CategoryService.class);
        CategoryController categoryController = new CategoryController(categoryService);
        Long nonExistentId = 999L;
        CategoryDTO categoryDTO = new CategoryDTO(nonExistentId, "Non-existent Category", 1L);
        when(categoryService.updateCategory(nonExistentId, categoryDTO)).thenReturn(null);

        ResponseEntity<CategoryDTO> response = categoryController.updateCategory(nonExistentId, categoryDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

}