package com.abishek.financeapi.Service.Wallet;

import java.util.List;

import com.abishek.financeapi.DTO.BillDTO;
import com.abishek.financeapi.DTO.PayBillDTO;
import com.abishek.financeapi.DTO.TransactionDetailsDTO;
import com.abishek.financeapi.DTO.WalletDTO;

public interface WalletService {
//    void addMoneyToWallet(Long userId, double amount) throws Exception;
//    void payBillUsingWallet(Long userId, Long billId) throws Exception;
//    void recordTransaction(Long walletId, String paymentMethod, String paymentId, double amount);
    WalletDTO getWalletByUserId(Long userId);
    List<TransactionDetailsDTO> getTransactionDetails(Long walletId);
	List<TransactionDetailsDTO> getTransactionDetailsByUserId(Long userId);
	void addMoneyToWallet(WalletDTO walletDTO) throws Exception;
//	void payBillUsingWallet(BillDTO billDTO) throws Exception;
	void payBillUsingWallet(PayBillDTO payDTO) throws Exception;
//	BillDTO findBillID(Long billId, Long userId);
	List<TransactionDetailsDTO> getAllTransactionDetailsByDescending(Long walletId);
}

