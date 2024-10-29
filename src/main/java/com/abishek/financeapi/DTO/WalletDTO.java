package com.abishek.financeapi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletDTO {
    private Long id;
    private double amount;
    private double balance;    
    private Long categoryId;
    private Long userId;
}
