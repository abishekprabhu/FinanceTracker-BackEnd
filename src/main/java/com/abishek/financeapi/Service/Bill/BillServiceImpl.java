package com.abishek.financeapi.Service.Bill;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abishek.financeapi.DTO.BillDTO;
import com.abishek.financeapi.DTO.TransactionDetailsDTO;
import com.abishek.financeapi.Exception.BillNotFoundException;
import com.abishek.financeapi.Exception.CategoryNotFoundException;
import com.abishek.financeapi.Exception.UserNotFoundException;
import com.abishek.financeapi.Model.Bill;
import com.abishek.financeapi.Model.Category;
import com.abishek.financeapi.Model.TransactionDetails;
import com.abishek.financeapi.Model.User;
import com.abishek.financeapi.Repository.BillRepository;
import com.abishek.financeapi.Repository.CategoryRepository;
import com.abishek.financeapi.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService{
	
	 @Autowired
	    private BillRepository billRepository;

	    @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private CategoryRepository categoryRepository;

	    @Override
	    public BillDTO createBill(BillDTO billDTO) {
	        // Map DTO to model
	        Bill bill = mapToBill(billDTO);

	        // Save the bill
	        billRepository.save(bill);

	        // Map saved model to DTO
	        return mapToBillDTO(bill);
	    }

	    // Mapper method from BillDTO to Bill
	    private Bill mapToBill(BillDTO billDTO) {
	        Bill bill = new Bill();
	        bill.setDescription(billDTO.getDescription());
	        bill.setAmountDue(billDTO.getAmountDue());
	        bill.setDueDate(billDTO.getDueDate());
	        bill.setPaid(false);  // Assuming new bills are unpaid by default

	        // Fetch the user and set it to the bill
	        User user = userRepository.findById(billDTO.getUserId())
	                .orElseThrow(() -> new UserNotFoundException("User not found"));
	        bill.setUser(user);
	        
	        Category category = categoryRepository.findById(billDTO.getCategoryId())
	                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
	        
	        bill.setCategory(category);

	        return bill;
	    }

	    // Mapper method from Bill to BillDTO
	    private BillDTO mapToBillDTO(Bill bill) {
	        BillDTO billDTO = new BillDTO();
	        billDTO.setId(bill.getId());
	        billDTO.setDescription(bill.getDescription());
	        billDTO.setAmountDue(bill.getAmountDue());
	        billDTO.setDueDate(bill.getDueDate());
	        billDTO.setUserId(bill.getUser().getId());
	        billDTO.setCategoryId(bill.getCategory().getId());

	        return billDTO;
	    }
	    
	    @Override
	    public List<BillDTO> getAllBills(Long userId) {
	        List<Bill> bills = billRepository.findByUserId(userId);
	        return bills.stream().map(bill -> {
	            BillDTO dto = new BillDTO();
	            dto.setId(bill.getId());
	            dto.setDescription(bill.getDescription());
	            dto.setAmountDue(bill.getAmountDue());
	            dto.setDueDate(bill.getDueDate());
	            dto.setPaid(bill.isPaid());
	            dto.setUserId(bill.getUser().getId());
	            dto.setCategoryId(bill.getCategory().getId());
	            dto.setWalletId(bill.getWallet() != null ? bill.getWallet().getId() : null);
	            return dto;
	        }).collect(Collectors.toList());
	    }

	    @Override
	    public List<BillDTO> getUnpaidBills(Long userId) {
	        List<Bill> unpaidBills = billRepository.findByIsPaidFalse();
	        return unpaidBills.stream()
	        		.sorted(Comparator.comparing(Bill::getDueDate).reversed())
	        		.map(bill -> {
	            BillDTO dto = new BillDTO();
	            dto.setId(bill.getId());
	            dto.setDescription(bill.getDescription());
	            dto.setAmountDue(bill.getAmountDue());
	            dto.setDueDate(bill.getDueDate());
	            dto.setPaid(bill.isPaid());
	            dto.setUserId(bill.getUser().getId());
	            dto.setCategoryId(bill.getCategory().getId());
	            dto.setWalletId(bill.getWallet() != null ? bill.getWallet().getId() : null);
	            return dto;
	        }).collect(Collectors.toList());
	    }   
	    
	    @Override
	    public BillDTO updateBill(Long billId, BillDTO billDTO) {
	        Bill bill = billRepository.findById(billId)
	                .orElseThrow(() -> new BillNotFoundException("Bill not found"));

	        bill.setDescription(billDTO.getDescription());
	        bill.setAmountDue(billDTO.getAmountDue());
	        bill.setDueDate(billDTO.getDueDate());
	        bill.setPaid(billDTO.isPaid());

	        User user = userRepository.findById(billDTO.getUserId())
	                .orElseThrow(() -> new UserNotFoundException("User not found"));
	        bill.setUser(user);

	        Category category = categoryRepository.findById(billDTO.getCategoryId())
	                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
	        bill.setCategory(category);

	        billRepository.save(bill);
	        return mapToBillDTO(bill);
	    }

	    @Override
	    public void deleteBill(Long billId) {
	        Bill bill = billRepository.findById(billId)
	                .orElseThrow(() -> new BillNotFoundException("Bill not found"));
	        billRepository.delete(bill);
	    }
	    
	    
	    @Override
	    public BillDTO findByBillID(Long billId, Long userId) {
	        Optional<Bill> bill = billRepository.findByIdAndUserId(billId, userId);
	        
	        if (bill.isPresent()) {
	            Bill foundBill = bill.get();
	            log.info("Bill found: id={}, amountDue={}, description={}, dueDate={}, isPaid={}",
	                     foundBill.getId(), foundBill.getAmountDue(), foundBill.getDescription(),
	                     foundBill.getDueDate(), foundBill.isPaid());
	            
	            // Continue with mapping to DTO
	            BillDTO billDTO = mapToDTO(foundBill);
	            return billDTO;
	        } else {
	            log.warn("No bill found for id: {} and userId: {}", billId, userId);
	            return null;
	        }
	    }

	    
	    private BillDTO mapToDTO(Bill bill) {
	        BillDTO dto = new BillDTO();
	        dto.setId(bill.getId());
	        dto.setAmountDue(bill.getAmountDue());
	        dto.setDescription(bill.getDescription());
	        dto.setDueDate(bill.getDueDate());
	        dto.setPaid(bill.isPaid());
	        dto.setCategoryId(bill.getCategory().getId());
	        dto.setUserId(bill.getUser().getId());
	        // Map other fields as needed
	        return dto;
	    }
	    
}
