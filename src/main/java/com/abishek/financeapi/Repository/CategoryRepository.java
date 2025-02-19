package com.abishek.financeapi.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abishek.financeapi.Model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByName(String name);	
    
    List<Category> findByUserIdAndExpensesIsNotNull(Long userId);
    
    List<Category> findByUserIdAndIncomesIsNotNull(Long userId);

    Optional<Category> findByNameAndUserId(String name, Long userId);

    List<Category> findByUserId(Long userId);
}
