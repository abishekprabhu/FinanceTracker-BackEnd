package com.abishek.financeapi.Service.Wallet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abishek.financeapi.Controller.NotificationController;
import com.abishek.financeapi.DTO.BillDTO;
import com.abishek.financeapi.DTO.PayBillDTO;
import com.abishek.financeapi.DTO.TransactionDTO;
import com.abishek.financeapi.DTO.TransactionDetailsDTO;
import com.abishek.financeapi.DTO.WalletDTO;
import com.abishek.financeapi.Enum.TransactionType;
import com.abishek.financeapi.Exception.BillNotFoundException;
import com.abishek.financeapi.Exception.CategoryNotFoundException;
import com.abishek.financeapi.Exception.CustomException;
import com.abishek.financeapi.Exception.ExpenseNotFoundException;
import com.abishek.financeapi.Exception.InsufficientBalanceException;
import com.abishek.financeapi.Exception.UserNotFoundException;
import com.abishek.financeapi.Exception.WalletNotFoundException;
import com.abishek.financeapi.Model.Bill;
import com.abishek.financeapi.Model.Category;
import com.abishek.financeapi.Model.Expense;
import com.abishek.financeapi.Model.Income;
import com.abishek.financeapi.Model.Transaction;
import com.abishek.financeapi.Model.TransactionDetails;
import com.abishek.financeapi.Model.User;
import com.abishek.financeapi.Model.Wallet;
import com.abishek.financeapi.Repository.BillRepository;
import com.abishek.financeapi.Repository.CategoryRepository;
import com.abishek.financeapi.Repository.ExpenseRepository;
import com.abishek.financeapi.Repository.IncomeRepository;
import com.abishek.financeapi.Repository.TransactionDetailsRepository;
import com.abishek.financeapi.Repository.TransactionRepository;
import com.abishek.financeapi.Repository.UserRepository;
import com.abishek.financeapi.Repository.WalletRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
	
    @Autowired
    private NotificationController notificationController;
    
	@Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionDetailsRepository transactionDetailRepository;
    
    private final BillRepository billRepository;
    
    private final IncomeRepository incomeRepository;
    
    private final TransactionRepository transactionRepository;
    
    private final UserRepository userRepository;
    
    private final CategoryRepository categoryRepository;
    
    private final ExpenseRepository expenseRepository;
    
	private static final String KEY ="rzp_test_t51Ev8vhVfMTFO";
	private static final String KEY_SECRET ="74wo7RymLNZaB5fYFQDe7uae";
	private static final String CURRENCY = "INR";
    
    @Override
    public void addMoneyToWallet(WalletDTO walletDTO) throws Exception {
    	 // Fetch the user and wallet
        User user = userRepository.findById(walletDTO.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Category category = categoryRepository.findById(walletDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        // Fetch all incomes for the user
        List<Income> incomes = incomeRepository.findByUserId(walletDTO.getUserId());
        
//        // Calculate total income
//        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
//
//        // Check if sufficient income exists to deduct the amount
//        if (totalIncome < walletDTO.getAmount()) {
//            throw new InsufficientBalanceException("Insufficient total income to add money to the wallet.");
//        }
        
        double currentBalance = getUserCurrentBalance(user); // Method to get the user's balance
        if (currentBalance < walletDTO.getAmount()) {
            throw new InsufficientBalanceException("Insufficient total income to add money to the wallet.");
        }
        
        // Fetch the wallet for the user, or create a new one if it doesn't exist
        Wallet wallet = walletRepository.findByUserId(walletDTO.getUserId());
        if (wallet == null) {
            // Create a new wallet if not found
            wallet = new Wallet();
            wallet.setUser(user);
            wallet.setCategory(category);
            wallet.setBalance(0.0);  // Initialize balance to 0
            wallet = walletRepository.save(wallet);
        }
        
        // Add money to the wallet balance
        wallet.setBalance(wallet.getBalance() + walletDTO.getAmount());
        
     // Razorpay integration
        System.out.println("Initializing Razorpay client");
	    try {
	        RazorpayClient razorpay = new RazorpayClient(KEY, KEY_SECRET);
	        
	        // Generate a receipt for the transaction
//	        String receipt = "txn_" + wallet.getId();
	        // Generate a unique receipt using UUID
	        String receipt = "txn_" + UUID.randomUUID().toString();

	        // Create Razorpay order
	        JSONObject options = new JSONObject();
	        options.put("amount", (int)(walletDTO.getAmount() * 100));  // Convert amount to paise
	        options.put("currency", CURRENCY);
	        options.put("receipt", receipt);

	        Order order = razorpay.orders.create(options);
	        log.info("RazorPay : "+order);
	        
	        // Record the transaction details, including the receipt
	        TransactionDetailsDTO transactionDetailsDTO = new TransactionDetailsDTO();
	        transactionDetailsDTO.setPaymentMethod("ADD MONEY TO WALLET");
	        transactionDetailsDTO.setPaymentId(order.get("id"));
		     // Cast the amount to Integer and convert to rupees (double)
	        Integer amountInPaise = (Integer) order.get("amount");  // Cast the amount to Integer
	        double amountInRupees = amountInPaise / 100.0;  // Convert paise to rupees (double)
	        transactionDetailsDTO.setAmount(amountInRupees);
	        transactionDetailsDTO.setWalletId(wallet.getId());
	        transactionDetailsDTO.setReceipt(receipt);
	        transactionDetailsDTO.setTransactionDate(LocalDateTime.now());
	        
	        // Save transaction details using the DTO mapping
	        transactionDetailRepository.save(mapToTransactionDetails(transactionDetailsDTO));
	        log.info("Transaction Details: " + transactionDetailsDTO.toString());

	        // Save the transaction details
//	        try {
//	            transactionDetailRepository.save(transaction);
//	            log.info("Transaction saved successfully: " + transaction.toString());
//	        } catch (Exception e) {
//	            log.error("Error saving transaction: " + e.getMessage());
//	            throw new RuntimeException("Transaction saving failed", e);
//	        }


	    } catch (RazorpayException e) {
	    	 log.info("Razorpay initialization failed" +e.getMessage());
	        throw new RuntimeException("Error processing payment with Razorpay",e);
	       
	    }

	    // Map the wallet DTO 
//	    WalletDTO walletDTO = new WalletDTO();
	    walletDTO.setId(wallet.getId());
	    walletDTO.setBalance(wallet.getBalance());
	    walletDTO.setUserId(walletDTO.getUserId());
	    walletDTO.setCategoryId(walletDTO.getCategoryId());
	    
	    // Save wallet using the DTO mapping
	    walletRepository.save(mapToWallet(walletDTO));
	    log.info("walleDto : "+ walletDTO.toString());
        
        // Create Transaction record
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setDescription("Added money to wallet");
        transactionDTO.setAmount(walletDTO.getAmount());
        transactionDTO.setType(TransactionType.EXPENSE);
        transactionDTO.setDate(LocalDate.now());
        transactionDTO.setUserId(user.getId());
        transactionDTO.setCategoryId(category.getId());
        transactionDTO.setWalletId(wallet.getId());        
        // Save transaction

        Transaction transaction = new Transaction();
        if (transactionDTO.getType() == TransactionType.EXPENSE && transactionDTO.getExpenseId() == null) {
//            double currentBalance = getUserCurrentBalance(user); // Method to get the user's balance
            if (currentBalance < transactionDTO.getAmount()) {
                throw new InsufficientBalanceException("Insufficient balance to complete the transaction.");
            }
            Expense newExpense = new Expense();
            newExpense.setDescription(transactionDTO.getDescription());
            newExpense.setAmount(transactionDTO.getAmount());
            newExpense.setDate(transactionDTO.getDate());
            newExpense.setUser(user);
            newExpense.setCategory(category);
//            Expense savedExpense =
            		expenseRepository.save(newExpense);
        } else if (transactionDTO.getExpenseId() != null) {
            Expense expense = expenseRepository.findById(transactionDTO.getExpenseId())
                    .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));
            transaction.setExpense(expense);
        }
        
        transactionRepository.save(mapToTransaction(transactionDTO));
        log.info("transaction : "+ transactionDTO.toString());
        
        // Send notifications
        double curBalance = getUserCurrentBalance(user);
        notificationController.sendExpenseNotification(transactionDTO.getAmount(),curBalance);
        notificationController.sendWalletNotification(transactionDTO.getAmount(),wallet.getBalance());
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
    @Transactional
    public void payBillUsingWallet(PayBillDTO payDTO) throws Exception {
        log.info("Entering payBillUsingWallet method with payDTO: {}", payDTO);

        User user = userRepository.findById(payDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        // Fetch the bill by billId and userId
        log.info("Fetching bill");
        Optional<Bill> billOptional = billRepository.findByIdAndUserId(payDTO.getBillId(), payDTO.getUserId());

        if (billOptional.isEmpty()) {
            throw new BillNotFoundException("Bill not found.");
        }
        
        Bill bill = billOptional.get();
        
        // Convert Bill to BillDTO
        BillDTO billDTO = mapToBillDTO(bill);
        log.info("Bill details: {}", billDTO);

        // Check wallet balance
        Wallet wallet = walletRepository.findByUserId(user.getId());
        
        if (wallet == null) {
            throw new WalletNotFoundException("Wallet not found.");
        }
        if (wallet.getBalance() <= 0) {
            throw new InsufficientBalanceException("Insufficient balance in Wallet.");
        }

        double amountDue = billDTO.getAmountDue();
        
        if (amountDue > wallet.getBalance()) {
            throw new InsufficientBalanceException("Insufficient wallet balance to pay the bill.");
        }

        // Deduct the amount from the wallet
        wallet.setBalance(wallet.getBalance() - amountDue);
        walletRepository.save(wallet);
        
        // Mark the bill as paid
        bill.setPaid(true);
        billRepository.save(bill);
        
        // Log the updated bill information using DTO
        BillDTO updatedBillDTO = mapToBillDTO(bill);
        log.info("Updated Bill details: {}", updatedBillDTO);

        // Razorpay payment logic
        try {
            RazorpayClient razorpay = new RazorpayClient(KEY, KEY_SECRET);
            
            String receipt = "txn_" + UUID.randomUUID().toString();
            JSONObject options = new JSONObject();
            options.put("amount", (int) (amountDue * 100));  // Convert to paise
            options.put("currency", CURRENCY);
            options.put("receipt", receipt);
            
            Order order = razorpay.orders.create(options);
            
            TransactionDetailsDTO transactionDetailsDTO = new TransactionDetailsDTO();
            transactionDetailsDTO.setPaymentMethod("PAY BILLS USING RAZORPAY");
            transactionDetailsDTO.setPaymentId(order.get("id"));
            double amountInRupees = ((Integer) order.get("amount")) / 100.0;
            transactionDetailsDTO.setAmount(amountInRupees);
            transactionDetailsDTO.setWalletId(wallet.getId());
            transactionDetailsDTO.setReceipt(receipt);
            transactionDetailsDTO.setTransactionDate(LocalDateTime.now());
            
            transactionDetailRepository.save(mapToTransactionDetails(transactionDetailsDTO));
            log.info("Transaction Details: {}", transactionDetailsDTO);
            
        } catch (RazorpayException e) {
            throw new RuntimeException("Error processing payment with Razorpay", e);
        }

        // Record the transaction
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setDescription("Bill payment");
        transactionDTO.setAmount(amountDue);
        transactionDTO.setType(TransactionType.EXPENSE);
        transactionDTO.setDate(LocalDate.now());
        transactionDTO.setUserId(billDTO.getUserId());
        transactionDTO.setCategoryId(billDTO.getCategoryId());
        transactionDTO.setWalletId(wallet.getId());

        transactionRepository.save(mapToTransaction(transactionDTO));
        log.info("Transaction recorded: {}", transactionDTO);
        
        // Send notifications
//      
        notificationController.sendBillNotification(transactionDTO.getAmount(),wallet.getBalance());
    }


    @Override
    public WalletDTO getWalletByUserId(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId);
        WalletDTO walletDTO = new WalletDTO();
        walletDTO.setId(wallet.getId());
        walletDTO.setBalance(wallet.getBalance());
        walletDTO.setUserId(wallet.getUser().getId());
        walletDTO.setCategoryId(wallet.getCategory().getId());
        return walletDTO;
    }
    
    @Override
    public List<TransactionDetailsDTO> getTransactionDetailsByUserId(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId);
        if (wallet == null) {
            throw new WalletNotFoundException("Wallet not found for the user");
        }
        return getTransactionDetails(wallet.getId());
    }

    @Override
    public List<TransactionDetailsDTO> getTransactionDetails(Long walletId) {
        List<TransactionDetails> transactions = transactionDetailRepository.findByWalletId(walletId);
        return transactions.stream().map(transaction -> {
            TransactionDetailsDTO dto = new TransactionDetailsDTO();
            dto.setId(transaction.getId());
            dto.setPaymentMethod(transaction.getPaymentMethod());
            dto.setPaymentId(transaction.getPaymentId());
            dto.setReceipt(transaction.getReceipt());
            dto.setAmount(transaction.getAmount());
            dto.setTransactionDate(transaction.getTransactionDate());
            dto.setWalletId(transaction.getWallet().getId());
            return dto;
        }).collect(Collectors.toList());
    }
    
	@Override
	public List<TransactionDetailsDTO> getAllTransactionDetailsByDescending(Long walletId){	
		
		List<TransactionDetails> transactions = transactionDetailRepository.findByWalletId(walletId);
		return transactions.stream()
				.sorted(Comparator.comparing(TransactionDetails::getTransactionDate).reversed())
				.map(transaction -> {
		            TransactionDetailsDTO dto = new TransactionDetailsDTO();
		            dto.setId(transaction.getId());
		            dto.setPaymentMethod(transaction.getPaymentMethod());
		            dto.setPaymentId(transaction.getPaymentId());
		            dto.setReceipt(transaction.getReceipt());
		            dto.setAmount(transaction.getAmount());
		            dto.setTransactionDate(transaction.getTransactionDate());
		            dto.setWalletId(transaction.getWallet().getId());
		            return dto;
		        }).collect(Collectors.toList());	
	}
    
    
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

        if (transactionDTO.getWalletId() != null) {
            Wallet wallet = walletRepository.findById(transactionDTO.getWalletId())
                    .orElseThrow(() -> new CustomException("Wallet not found"));
            transaction.setWallet(wallet);
        }

        return transaction;
    }
    
    private Wallet mapToWallet(WalletDTO walletDTO) {
        Wallet wallet = new Wallet();
        wallet.setId(walletDTO.getId());
        wallet.setBalance(walletDTO.getBalance());

        User user = userRepository.findById(walletDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        wallet.setUser(user);

        if (walletDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(walletDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            wallet.setCategory(category);
        }

        return wallet;
    }
    
    private TransactionDetails mapToTransactionDetails(TransactionDetailsDTO transactionDTO) {
        TransactionDetails transaction = new TransactionDetails();
        transaction.setPaymentMethod(transactionDTO.getPaymentMethod());
        transaction.setPaymentId(transactionDTO.getPaymentId());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setReceipt(transactionDTO.getReceipt());
        transaction.setTransactionDate(transactionDTO.getTransactionDate());

        if (transactionDTO.getWalletId() != null) {
            Wallet wallet = walletRepository.findById(transactionDTO.getWalletId())
                    .orElseThrow(() -> new RuntimeException("Wallet not found"));
            transaction.setWallet(wallet);
        }

        return transaction;
    }    
    

    public BillDTO mapToBillDTO(Bill bill) {
        BillDTO billDTO = new BillDTO();
        billDTO.setId(bill.getId());
        billDTO.setAmountDue(bill.getAmountDue());
        billDTO.setDescription(bill.getDescription());
        billDTO.setDueDate(bill.getDueDate());
        billDTO.setPaid(bill.isPaid());
        billDTO.setUserId(bill.getUser().getId());
        billDTO.setWalletId(bill.getWallet() != null ? bill.getWallet().getId() : null);
        billDTO.setCategoryId(bill.getCategory() != null ? bill.getCategory().getId() : null);
        return billDTO;
    }




}
