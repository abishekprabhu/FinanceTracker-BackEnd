package com.abishek.financeapi.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailsDTO {
    private Long id;
    private String paymentMethod;
    private String paymentId;
    private double amount;
    private String receipt;
    private LocalDateTime transactionDate;
    private Long walletId;
//    private Long billId;
    
}
