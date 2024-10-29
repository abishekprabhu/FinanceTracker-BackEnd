package com.abishek.financeapi.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BillDTO {
    private Long id;
    private String description;
    private double amountDue;
    private LocalDate dueDate;
    private boolean isPaid;
    private Long userId;
    private Long categoryId;
    private Long walletId;
//    private Long transactionDetailsId;
}
