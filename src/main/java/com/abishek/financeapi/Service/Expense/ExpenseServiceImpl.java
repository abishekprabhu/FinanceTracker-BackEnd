package com.abishek.financeapi.Service.Expense;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abishek.financeapi.DTO.ExpenseDTO;
import com.abishek.financeapi.DTO.TransactionDTO;
import com.abishek.financeapi.Enum.TransactionType;
import com.abishek.financeapi.Exception.CategoryNotFoundException;
import com.abishek.financeapi.Exception.ExpenseNotFoundException;
import com.abishek.financeapi.Exception.TransactionNotFoundException;
import com.abishek.financeapi.Exception.UserNotFoundException;
import com.abishek.financeapi.Model.Category;
import com.abishek.financeapi.Model.Expense;
import com.abishek.financeapi.Model.Transaction;
import com.abishek.financeapi.Model.User;
import com.abishek.financeapi.Repository.CategoryRepository;
import com.abishek.financeapi.Repository.ExpenseRepository;
import com.abishek.financeapi.Repository.IncomeRepository;
import com.abishek.financeapi.Repository.TransactionRepository;
import com.abishek.financeapi.Repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService{
	
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ExpenseDTO createExpense(ExpenseDTO expenseDTO) {
        Expense expense = new Expense();
        expense.setDescription(expenseDTO.getDescription());
        expense.setAmount(expenseDTO.getAmount());
        expense.setDate(expenseDTO.getDate());

        User user = userRepository.findById(expenseDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Category category = categoryRepository.findById(expenseDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        expense.setUser(user);
        expense.setCategory(category);

        Expense savedExpense = expenseRepository.save(expense);

        // Create Transaction record
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setDescription(expense.getDescription());
        transactionDTO.setAmount(expense.getAmount());
        transactionDTO.setType(TransactionType.EXPENSE);
        transactionDTO.setDate(expense.getDate());
        transactionDTO.setUserId(user.getId());
        transactionDTO.setCategoryId(category.getId());
        transactionDTO.setExpenseId(savedExpense.getId());

        // Save transaction
        transactionRepository.save(mapToTransaction(transactionDTO));

        return mapToDTO(savedExpense);
    }
	
    @Override
    public ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO) {
        // Fetch the expense to be updated
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));

        // Update expense details
        expense.setDescription(expenseDTO.getDescription());
        expense.setAmount(expenseDTO.getAmount());
        expense.setDate(expenseDTO.getDate());

        // Fetch the user and category
        User user = userRepository.findById(expenseDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Category category = categoryRepository.findById(expenseDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        // Update the expense with user and category details
        expense.setUser(user);
        expense.setCategory(category);

        // Save the updated expense
        Expense updatedExpense = expenseRepository.save(expense);

        // Now update the associated transaction
        Optional<Transaction> optionalTransaction = transactionRepository.findByExpenseId(updatedExpense.getId());
        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            transaction.setDescription(updatedExpense.getDescription());
            transaction.setAmount(updatedExpense.getAmount());
            transaction.setDate(updatedExpense.getDate());
            transaction.setUser(updatedExpense.getUser());
            transaction.setCategory(updatedExpense.getCategory());
            
            // If the transaction relates to an expense, we set the expenseId
            transaction.setExpense(updatedExpense);

            // Save the updated transaction
            transactionRepository.save(transaction);
        } else {        	
        	throw new ExpenseNotFoundException("Expense id Not Found");
        }

        return mapToDTO(updatedExpense);
    }

    
    @Override
    public List<ExpenseDTO> getAllExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        return expenses.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
	
	
	@Override
	public List<ExpenseDTO> getAllExpensesByDescending(){		
		return expenseRepository.findAll().stream()
				.sorted(Comparator.comparing(Expense::getDate).reversed())
				.map(this::mapToDTO)
				.collect(Collectors.toList());		
	}
	
    @Override
    public ExpenseDTO getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
        return mapToDTO(expense);
    }
	
    
    @Override
    @Transactional
    public void deleteExpense(Long id) {
        // Find the expense by ID
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found in this id : " + id));

        // Find the associated transaction by expense ID
        Optional<Transaction> optionalTransaction = transactionRepository.findByExpenseId(expense.getId());
        
        // If the transaction exists, delete it
        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            transactionRepository.delete(transaction);
        } else {
            throw new TransactionNotFoundException("Transaction not found for expense ID: " + id);
        }

        // Now delete the expense itself
        expenseRepository.deleteById(id);
    }

	
	
    private ExpenseDTO mapToDTO(Expense expense) {
        return new ExpenseDTO(expense.getId(), expense.getDescription(), expense.getAmount(),
                expense.getDate(), expense.getUser().getId(), expense.getCategory().getId());
    }
    
    // Helper method to map TransactionDTO to Transaction entity
    private Transaction mapToTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(transactionDTO.getType());
        transaction.setDate(transactionDTO.getDate());

        User user = userRepository.findById(transactionDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        transaction.setUser(user);
        transaction.setCategory(category);

        if (transactionDTO.getExpenseId() != null) {
            Expense expense = expenseRepository.findById(transactionDTO.getExpenseId())
                    .orElseThrow(() -> new RuntimeException("Expense not found"));
            transaction.setExpense(expense);
        }

        return transaction;
    }
    
    @Override
    public List<ExpenseDTO> getAllExpenseByUserId(Long userId) {
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        return expenses.stream()
        		.sorted(Comparator.comparing(Expense::getDate).reversed())
        		.map(this::mapToDTO)
        		.collect(Collectors.toList());
    }
    
    

    @Override
    public List<ExpenseDTO> getAllExpenseByUserIdAndCategoryId(Long userId, Long categoryId) {
        List<Expense> expenses = expenseRepository.findByUserIdAndCategoryId(userId, categoryId);
        return expenses.stream().map(this::mapToDTO).collect(Collectors.toList());
    }



}
