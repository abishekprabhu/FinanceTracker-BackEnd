package com.abishek.financeapi.Service.Income;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abishek.financeapi.Controller.NotificationController;
import com.abishek.financeapi.DTO.ExpenseDTO;
import com.abishek.financeapi.DTO.IncomeDTO;
import com.abishek.financeapi.DTO.TransactionDTO;
import com.abishek.financeapi.Enum.TransactionType;
import com.abishek.financeapi.Exception.CategoryNotFoundException;

import com.abishek.financeapi.Exception.IncomeNotFoundException;
import com.abishek.financeapi.Exception.TransactionNotFoundException;
import com.abishek.financeapi.Exception.UserNotFoundException;
import com.abishek.financeapi.Model.Category;
import com.abishek.financeapi.Model.Expense;
import com.abishek.financeapi.Model.Income;
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
public class IncomeServiceImpl implements IncomeService{
	
    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired  
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private NotificationController notificationController;
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Override
    @Transactional
    public IncomeDTO createIncome(IncomeDTO incomeDTO) {
        Income income = new Income();
        income.setDescription(incomeDTO.getDescription());
        income.setAmount(incomeDTO.getAmount());
        income.setDate(incomeDTO.getDate());

        User user = userRepository.findById(incomeDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Category category = categoryRepository.findById(incomeDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        income.setUser(user);
        income.setCategory(category);

        Income savedIncome = incomeRepository.save(income);

        // Create Transaction record
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setDescription(income.getDescription());
        transactionDTO.setAmount(income.getAmount());
        transactionDTO.setType(TransactionType.INCOME);
        transactionDTO.setDate(income.getDate());
        transactionDTO.setUserId(user.getId());
        transactionDTO.setCategoryId(category.getId());
        transactionDTO.setIncomeId(savedIncome.getId());

        // Save transaction
        transactionRepository.save(mapToTransaction(transactionDTO));
        
     // Send notifications
        double currentBalance = getUserCurrentBalance(user);
        notificationController.sendIncomeNotification(savedIncome.getAmount(),currentBalance);

        return mapToDTO(savedIncome);
    }
	
	public List<IncomeDTO> getAllIncome(){
		return incomeRepository.findAll().stream()
				.map(this::mapToDTO)
				.collect(Collectors.toList());
	}
	
	public List<IncomeDTO> getAllIncomeByDescending(){
		return incomeRepository.findAll().stream()
				.sorted(Comparator.comparing(Income::getDate).reversed())
				.map(this::mapToDTO)
				.collect(Collectors.toList());
	}
	
	public IncomeDTO getIncomeById(Long id) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new IncomeNotFoundException("Income is not present in this id : "+ id));
        return mapToDTO(income);
	}
	
	
    @Override
    public IncomeDTO updateIncome(Long id, IncomeDTO incomeDTO) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Income not found"));

        income.setDescription(incomeDTO.getDescription());
        income.setAmount(incomeDTO.getAmount());
        income.setDate(incomeDTO.getDate());

        User user = userRepository.findById(incomeDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Category category = categoryRepository.findById(incomeDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        income.setUser(user);
        income.setCategory(category);

        Income updatedIncome = incomeRepository.save(income);

        // Now update the associated transaction
        Optional<Transaction> optionalTransaction = transactionRepository.findByIncomeId(updatedIncome.getId());
        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            transaction.setDescription(updatedIncome.getDescription());
            transaction.setAmount(updatedIncome.getAmount());
            transaction.setDate(updatedIncome.getDate());
            transaction.setUser(updatedIncome.getUser());
            transaction.setCategory(updatedIncome.getCategory());
            
            // If the transaction relates to an income, we set the incomeId
            transaction.setIncome(updatedIncome);

            // Save the updated transaction
            transactionRepository.save(transaction);
        } else {        	
        	throw new IncomeNotFoundException("Income id Not Found");
        }
        
        // Send notifications
        double currentBalance = getUserCurrentBalance(user);
        notificationController.sendIncomeNotification(updatedIncome.getAmount(),currentBalance);

        return mapToDTO(updatedIncome);
    }
    
    @Override
    @Transactional
    public void deleteIncome(Long id) {
        // Find the income by ID
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new IncomeNotFoundException("Income not found in this id : " + id));

        // Find the associated transaction by income ID
        Optional<Transaction> optionalTransaction = transactionRepository.findByIncomeId(income.getId());
        
        // If the transaction exists, delete it
        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            transactionRepository.delete(transaction);
        } else {
            throw new TransactionNotFoundException("Transaction not found for income ID: " + id);
        }

     // Send notifications
        notificationController.sendNotifications("Income deleted successful details : "+income.getDescription() +"with amount" + income.getAmount());

        // Now delete the income itself
        incomeRepository.deleteById(id);
    }

	
    private IncomeDTO mapToDTO(Income income) {
        return new IncomeDTO(income.getId(), income.getDescription(), income.getAmount(),
                income.getDate(), income.getUser().getId(), income.getCategory().getId());
    }
	
    // Helper method to map TransactionDTO to Transaction entity
    private Transaction mapToTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(transactionDTO.getType());
        transaction.setDate(transactionDTO.getDate());

        User user = userRepository.findById(transactionDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Category category = categoryRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        transaction.setUser(user);
        transaction.setCategory(category);

        if (transactionDTO.getIncomeId() != null) {
            Income income = incomeRepository.findById(transactionDTO.getIncomeId())
                    .orElseThrow(() -> new IncomeNotFoundException("Income not found"));
            transaction.setIncome(income);
        }

        return transaction;
    }
    
	private double getUserCurrentBalance(User user) {
	    // Fetch total income and total expense for the user
	    Double totalIncome = incomeRepository.findTotalByUserId(user.getId());
	    Double totalExpense = expenseRepository.findTotalByUserId(user.getId());
	    
	    // If no income or no expense, handle nulls
	    totalIncome = totalIncome != null ? totalIncome : 0.0;
	    totalExpense = totalExpense != null ? totalExpense : 0.0;
	    
	    // Balance = Total Income - Total Expense
	    return totalIncome - totalExpense;
	}
    
	
    
    @Override
    public List<IncomeDTO> getAllIncomeByUserId(Long userId) {
        List<Income> incomes = incomeRepository.findByUserId(userId);
        return incomes.stream()
        		.sorted(Comparator.comparing(Income::getDate).reversed())
        		.map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<IncomeDTO> getAllIncomeByUserIdAndCategoryId(Long userId, Long categoryId) {
        List<Income> incomes = incomeRepository.findByUserIdAndCategoryId(userId, categoryId);
        return incomes.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
	
	
}
