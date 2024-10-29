package com.abishek.financeapi.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abishek.financeapi.DTO.TransactionDetailsDTO;
import com.abishek.financeapi.Service.Wallet.WalletService;

@RestController
@RequestMapping("/api/transaction-details")
@CrossOrigin("*")
public class TransactionDetailsController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<TransactionDetailsDTO>> getTransactionDetailsByWalletId(@PathVariable Long walletId) {
        List<TransactionDetailsDTO> transactionDetails = walletService.getTransactionDetails(walletId);
        if (transactionDetails.isEmpty()) {
            return ResponseEntity.noContent().build(); // No transactions found for the wallet
        }
        return ResponseEntity.ok(transactionDetails);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionDetailsDTO>> getTransactionDetailsByUserId(@PathVariable Long userId) {
        // Assuming there's a method to get transaction details by userId.
        // You can implement this in the service if required.
        List<TransactionDetailsDTO> transactionDetails = walletService.getTransactionDetailsByUserId(userId);
        if (transactionDetails.isEmpty()) {
            return ResponseEntity.noContent().build(); // No transactions found for the user
        }
        return ResponseEntity.ok(transactionDetails);
    }
}
