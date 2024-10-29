package com.abishek.financeapi.Service.Transaction;

import java.time.LocalDate;
import java.util.Comparator;
//import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abishek.financeapi.DTO.PdfDTO;
import com.abishek.financeapi.DTO.TransactionDTO;
import com.abishek.financeapi.Enum.TransactionType;
import com.abishek.financeapi.Exception.CategoryNotFoundException;
import com.abishek.financeapi.Exception.ExpenseNotFoundException;
import com.abishek.financeapi.Exception.IncomeNotFoundException;
import com.abishek.financeapi.Exception.InsufficientBalanceException;
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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(transactionDTO.getType());
        transaction.setDate(transactionDTO.getDate());

        // Fetch user and category
        User user = userRepository.findById(transactionDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Category category = categoryRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        transaction.setUser(user);
        transaction.setCategory(category);

        // Handling Income: if incomeId is null, create a new Income
        if (transactionDTO.getType() == TransactionType.INCOME && transactionDTO.getIncomeId() == null) {
            Income newIncome = new Income();
            newIncome.setDescription(transaction.getDescription());
            newIncome.setAmount(transaction.getAmount());
            newIncome.setDate(transaction.getDate());
            newIncome.setUser(user);
            newIncome.setCategory(category);
            Income savedIncome = incomeRepository.save(newIncome);
            transaction.setIncome(savedIncome);
        } else if (transactionDTO.getIncomeId() != null) {
            Income income = incomeRepository.findById(transactionDTO.getIncomeId())
                    .orElseThrow(() -> new IncomeNotFoundException("Income not found"));
            transaction.setIncome(income);
        }

        // Handling Expense: check balance before creating the expense
        if (transactionDTO.getType() == TransactionType.EXPENSE && transactionDTO.getExpenseId() == null) {
            double currentBalance = getUserCurrentBalance(user); // Method to get the user's balance
            if (currentBalance < transactionDTO.getAmount()) {
                throw new InsufficientBalanceException("Insufficient balance to complete the transaction.");
            }
            Expense newExpense = new Expense();
            newExpense.setDescription(transaction.getDescription());
            newExpense.setAmount(transaction.getAmount());
            newExpense.setDate(transaction.getDate());
            newExpense.setUser(user);
            newExpense.setCategory(category);
            Expense savedExpense = expenseRepository.save(newExpense);
            transaction.setExpense(savedExpense);
        } else if (transactionDTO.getExpenseId() != null) {
            Expense expense = expenseRepository.findById(transactionDTO.getExpenseId())
                    .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
            transaction.setExpense(expense);
        }

        // Save the transaction
        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToDTO(savedTransaction);
    }



    @Override
    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
        		.sorted(Comparator.comparing(Transaction::getDate).reversed())
        		.map(this::mapToDTO)        		
        		.collect(Collectors.toList());
    }

    @Override
    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        return mapToDTO(transaction);
    }

    @Override
    public TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));

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
        
        // Handling Expense: check balance before creating the expense
        if (transactionDTO.getExpenseId() != null) {
           double currentBalance = getUserCurrentBalance(user); // Method to get the user's balance
           if (currentBalance < transactionDTO.getAmount()) {
                throw new InsufficientBalanceException("Insufficient balance to complete the transaction.");
            }
            
	       Expense expense = expenseRepository.findById(transactionDTO.getExpenseId())
	                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
	       transaction.setExpense(expense);            
        }     

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToDTO(updatedTransaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));

        // If the transaction is associated with an Income, delete the Income
        if (transaction.getIncome() != null) {
            incomeRepository.deleteById(transaction.getIncome().getId());
        }

        // If the transaction is associated with an Expense, delete the Expense
        if (transaction.getExpense() != null) {
            expenseRepository.deleteById(transaction.getExpense().getId());
        }

        // Finally, delete the transaction itself
        transactionRepository.deleteById(id);
    }
    
    @Override
    public List<TransactionDTO> getAllTransactions(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        return transactions
        		.stream()
        		.sorted(Comparator.comparing(Transaction::getDate).reversed())
        		.map(this::mapToDTO)
        		.collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getTransactionsByCategory(Long userId, Long categoryId) {
        List<Transaction> transactions = transactionRepository.findByUserIdAndCategoryId(userId, categoryId);
        return transactions.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getTransactionsByType(Long userId, TransactionType type) {
        List<Transaction> transactions = transactionRepository.findByUserIdAndType(userId, type);
        return transactions.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    
	@Override
	public PdfDTO getTransactionDataMonthly() {
		LocalDate endDate = LocalDate.now();
		
		LocalDate startDate = endDate.minusDays(30);
		
		PdfDTO pdfDTO = new PdfDTO();
		pdfDTO.setTransactionList(transactionRepository.findByDateBetweenOrderByDateDesc(startDate, endDate));		
		return pdfDTO;
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


    private TransactionDTO mapToDTO(Transaction transaction) {
        return new TransactionDTO(
        		transaction.getId(),
        		transaction.getDescription(),
        		transaction.getAmount(),
                transaction.getType(),
                transaction.getDate(),
                transaction.getUser().getId(),
                transaction.getCategory().getId(), 
                transaction.getIncome() != null ? transaction.getIncome().getId() : null,
                transaction.getExpense() != null ? transaction.getExpense().getId() : null,
        		transaction.getWallet() != null ? transaction.getWallet().getId() : null);
        		}
}

