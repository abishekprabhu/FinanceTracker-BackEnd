package com.abishek.financeapi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
	  private String type; // e.g., "credit" or "balance"
	  private String message;
	  private double amount;
	  private double balance;
    
}
