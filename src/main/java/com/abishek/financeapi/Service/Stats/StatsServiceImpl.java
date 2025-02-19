package com.abishek.financeapi.Service.Stats;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import org.springframework.stereotype.Service;

import com.abishek.financeapi.DTO.GraphDTO;
import com.abishek.financeapi.DTO.MonthlyDataDTO;
import com.abishek.financeapi.DTO.PdfDTO;
import com.abishek.financeapi.DTO.PercentageDTO;
import com.abishek.financeapi.DTO.StatsDTO;
import com.abishek.financeapi.Model.Expense;
import com.abishek.financeapi.Model.Income;
import com.abishek.financeapi.Repository.ExpenseRepository;
import com.abishek.financeapi.Repository.IncomeRepository;
import com.abishek.financeapi.Repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

	private final IncomeRepository incomeRepository;
	
	private final ExpenseRepository expenseRepository;
	
	private final TransactionRepository transactionRepository;
	
	
	@Override
	public GraphDTO getChartDataMonthly(Long userId) {
		LocalDate endDate = LocalDate.now();
		
		LocalDate startDate = endDate.minusDays(30);
		
		GraphDTO graphDTO = new GraphDTO();
		graphDTO.setExpenseList(expenseRepository.findByUserIdAndDateBetweenOrderByDateAsc(userId,startDate, endDate));
		graphDTO.setIncomeList(incomeRepository.findByUserIdAndDateBetweenOrderByDateAsc(userId,startDate, endDate));
		
		return graphDTO;
	}
	
	@Override
	public GraphDTO getChartDataWeekly() {
		LocalDate endDate = LocalDate.now();
		
		LocalDate startDate = endDate.minusDays(7);
		
		GraphDTO graphDTO = new GraphDTO();
		graphDTO.setExpenseList(expenseRepository.findByDateBetweenOrderByDateAsc(startDate, endDate));
		graphDTO.setIncomeList(incomeRepository.findByDateBetweenOrderByDateAsc(startDate, endDate));
		
		return graphDTO;
	}
	
	@Override
	public GraphDTO getChartDataQuartarly() {
		LocalDate endDate = LocalDate.now();
		
		LocalDate startDate = endDate.minusDays(183);
		
		GraphDTO graphDTO = new GraphDTO();
		graphDTO.setExpenseList(expenseRepository.findByDateBetweenOrderByDateAsc(startDate, endDate));
		graphDTO.setIncomeList(incomeRepository.findByDateBetweenOrderByDateAsc(startDate, endDate));
		
		return graphDTO;
	}
	
	@Override
	public GraphDTO getChartDataYearly() {
		LocalDate endDate = LocalDate.now();
		
		LocalDate startDate = endDate.minusDays(365);
		
		GraphDTO graphDTO = new GraphDTO();
		graphDTO.setExpenseList(expenseRepository.findByDateBetweenOrderByDateAsc(startDate, endDate));
		graphDTO.setIncomeList(incomeRepository.findByDateBetweenOrderByDateAsc(startDate, endDate));
		
		return graphDTO;
	}
	
	@Override
	public PdfDTO getTransactionDataMonthly() {
		LocalDate endDate = LocalDate.now();
		
		LocalDate startDate = endDate.minusDays(30);
		
		PdfDTO pdfDTO = new PdfDTO();
		pdfDTO.setTransactionList(transactionRepository.findByDateBetweenOrderByDateDesc(startDate, endDate));		
		return pdfDTO;
	}

    @Override
    public StatsDTO getUserStats(Long userId){
        Double totalIncome = incomeRepository.SumAllAmountByUserId(userId);
        Double totalExpense = expenseRepository.SumAllAmountByUserId(userId);

        Optional<Income> optionalIncome = incomeRepository.findFirstByUserIdOrderByDateDesc(userId);
        Optional<Expense> optionalExpense = expenseRepository.findFirstByUserIdOrderByDateDesc(userId);

        StatsDTO statsDTO = new StatsDTO();
        statsDTO.setIncome(totalIncome);
        statsDTO.setExpense(totalExpense);

        optionalExpense.ifPresent(statsDTO::setLatestExpense);
        optionalIncome.ifPresent(statsDTO::setLatestIncome);

        statsDTO.setBalance(totalIncome-totalExpense);

        List<Income> incomeList = incomeRepository.findAllByUserId(userId);
        List<Expense> expenseList = expenseRepository.findAllByUserId(userId);

        OptionalDouble minIncome = incomeList.stream().mapToDouble(Income::getAmount).min();
        OptionalDouble maxIncome = incomeList.stream().mapToDouble(Income::getAmount).max();

        OptionalDouble minExpense = expenseList.stream().mapToDouble(Expense::getAmount).min();
        OptionalDouble maxExpense = expenseList.stream().mapToDouble(Expense::getAmount).max();

        statsDTO.setMaxIncome(maxIncome.isPresent() ? maxIncome.getAsDouble() : null);
        statsDTO.setMinIncome(minIncome.isPresent() ? minIncome.getAsDouble() : null);
        statsDTO.setMaxExpense(maxExpense.isPresent() ? maxExpense.getAsDouble() : null);
        statsDTO.setMinExpense(minExpense.isPresent() ? minExpense.getAsDouble() : null);

        return statsDTO;
    }
	
	@Override
    public PercentageDTO getTransactionSummary(Long userId) {
        // Define the current and previous months
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate startOfPreviousMonth = startOfMonth.minusMonths(1);

        // Get current month income and expenses
        List<Income> currentMonthIncomes = incomeRepository.findByUserIdAndDateBetween(userId, startOfMonth, now);
        List<Expense> currentMonthExpenses = expenseRepository.findByUserIdAndDateBetween(userId, startOfMonth, now);

        // Get previous month income and expenses
        List<Income> previousMonthIncomes = incomeRepository.findByUserIdAndDateBetween(userId, startOfPreviousMonth, startOfMonth.minusDays(1));
        List<Expense> previousMonthExpenses = expenseRepository.findByUserIdAndDateBetween(userId, startOfPreviousMonth, startOfMonth.minusDays(1));

        BigDecimal currentMonthIncomeTotal = currentMonthIncomes.stream()
        	    .map(income -> BigDecimal.valueOf(income.getAmount()))  // Convert Double to BigDecimal
        	    .reduce(BigDecimal.ZERO, BigDecimal::add);

        	BigDecimal currentMonthExpenseTotal = currentMonthExpenses.stream()
        	    .map(expense -> BigDecimal.valueOf(expense.getAmount()))
        	    .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal previousMonthIncomeTotal = previousMonthIncomes.stream()
        		.map(income -> BigDecimal.valueOf(income.getAmount()))
        		.reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal previousMonthExpenseTotal = previousMonthExpenses.stream()
        		.map(expense -> BigDecimal.valueOf(expense.getAmount()))
        		.reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate percentage changes
        BigDecimal incomePercentageChange = calculatePercentageChange(currentMonthIncomeTotal, previousMonthIncomeTotal);
        BigDecimal expensePercentageChange = calculatePercentageChange(currentMonthExpenseTotal, previousMonthExpenseTotal);

        // Balance change calculation
        BigDecimal currentMonthBalance = currentMonthIncomeTotal.subtract(currentMonthExpenseTotal);
        BigDecimal previousMonthBalance = previousMonthIncomeTotal.subtract(previousMonthExpenseTotal);
        BigDecimal balancePercentageChange = calculatePercentageChange(currentMonthBalance, previousMonthBalance);

        return new PercentageDTO(incomePercentageChange, expensePercentageChange, balancePercentageChange);
    }

	private BigDecimal calculatePercentageChange(BigDecimal current, BigDecimal previous) {
	    if (previous.equals(BigDecimal.ZERO)) {
	        if (current.equals(BigDecimal.ZERO)) {
	            return BigDecimal.ZERO; // No change, both months are zero
	        }
	        return BigDecimal.valueOf(100); // 100% increase if previous month was zero
	    }
	    return current.subtract(previous)
	                  .divide(previous, 2, BigDecimal.ROUND_HALF_UP)
	                  .multiply(BigDecimal.valueOf(100));
	}
	
	
//	@Override
//	public MonthlyDataDTO getMonthlyData() {
//	    LocalDate now = LocalDate.now();
//	    LocalDate startDate = LocalDate.of(now.getYear(), 1, 1); // Start of the year
//
//	    List<Income> incomeList = incomeRepository.findByDateBetween(startDate, now);
//	    List<Expense> expenseList = expenseRepository.findByDateBetween(startDate, now);
//
//	    MonthlyDataDTO monthlyData = new MonthlyDataDTO();
//	    Map<Integer, BigDecimal> incomeMap = new HashMap<>();
//	    Map<Integer, BigDecimal> expenseMap = new HashMap<>();
//
//	    // Aggregate income
//	    for (Income income : incomeList) {
//	        int month = income.getDate().getMonthValue();
//	        incomeMap.merge(month, BigDecimal.valueOf(income.getAmount()), BigDecimal::add);
//	    }
//
//	    // Aggregate expense
//	    for (Expense expense : expenseList) {
//	        int month = expense.getDate().getMonthValue();
//	        expenseMap.merge(month, BigDecimal.valueOf(expense.getAmount()), BigDecimal::add);
//	    }
//
//	    // Fill in the data
//	    monthlyData.setIncomeData(incomeMap);
//	    monthlyData.setExpenseData(expenseMap);
//	    return monthlyData;
//	}

	@Override
	public MonthlyDataDTO getMonthlyData(Long userId) {
		LocalDate now = LocalDate.now();
		LocalDate startDate = LocalDate.of(now.getYear(), 1, 1); // Start of the year

		List<Income> incomeList = incomeRepository.findByUserIdAndDateBetween(userId,startDate, now);
		List<Expense> expenseList = expenseRepository.findByUserIdAndDateBetween(userId,startDate, now);

		MonthlyDataDTO monthlyData = new MonthlyDataDTO();
		Map<Integer, BigDecimal> incomeMap = new HashMap<>();
		Map<Integer, BigDecimal> expenseMap = new HashMap<>();

		// Aggregate income
		for (Income income : incomeList) {
			int month = income.getDate().getMonthValue();
			incomeMap.merge(month, BigDecimal.valueOf(income.getAmount()), BigDecimal::add);
		}

		// Aggregate expense
		for (Expense expense : expenseList) {
			int month = expense.getDate().getMonthValue();
			expenseMap.merge(month, BigDecimal.valueOf(expense.getAmount()), BigDecimal::add);
		}

		// Fill in the data
		monthlyData.setIncomeData(incomeMap);
		monthlyData.setExpenseData(expenseMap);
		return monthlyData;
	}



	
}
