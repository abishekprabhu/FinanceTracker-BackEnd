package com.abishek.financeapi.Service.Bill;

import java.util.List;

import com.abishek.financeapi.DTO.BillDTO;


public interface BillService {
	
    BillDTO createBill(BillDTO billDTO);
//    BillDTO payBill(Long billId, Long userId) throws Exception;
    List<BillDTO> getAllBills(Long userId);
    List<BillDTO> getUnpaidBills(Long userId);
	BillDTO updateBill(Long billId, BillDTO billDTO);
	void deleteBill(Long billId);
	BillDTO findByBillID(Long billId, Long userId);
	
}
