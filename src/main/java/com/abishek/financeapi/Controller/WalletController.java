package com.abishek.financeapi.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abishek.financeapi.DTO.BillDTO;
import com.abishek.financeapi.DTO.PayBillDTO;
import com.abishek.financeapi.DTO.TransactionDetailsDTO;
import com.abishek.financeapi.DTO.WalletDTO;
import com.abishek.financeapi.Service.Wallet.WalletService;

@RestController
@RequestMapping("/api/wallet")
@CrossOrigin("*")
public class WalletController {

    @Autowired
    private WalletService walletService;
    
    @PostMapping("/add")
    public ResponseEntity<String> addMoney(@RequestBody WalletDTO walletDTO) {
        try {
            walletService.addMoneyToWallet(walletDTO);  // Pass the WalletDTO object to the service
            return ResponseEntity.ok("Money added to wallet successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/pay")
    public ResponseEntity<String> payBill(@RequestBody PayBillDTO billDTO) {
        try {
            walletService.payBillUsingWallet(billDTO);
            return ResponseEntity.ok("Bill paid successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<WalletDTO> getWallet(@PathVariable Long userId) {
        WalletDTO wallet = walletService.getWalletByUserId(userId);
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/transactions/{walletId}")
    public ResponseEntity<List<TransactionDetailsDTO>> getTransactionDetails(@PathVariable Long walletId) {
        List<TransactionDetailsDTO> transactions = walletService.getTransactionDetails(walletId);
        return ResponseEntity.ok(transactions);
    }    
    
    @GetMapping("/transactions/{walletId}/desc")
    public ResponseEntity<List<TransactionDetailsDTO>> getTransactionDetailsByDesc(@PathVariable Long walletId) {
        List<TransactionDetailsDTO> transactions = walletService.getAllTransactionDetailsByDescending(walletId);
        return ResponseEntity.ok(transactions);
    }    
    
}
