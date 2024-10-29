package com.abishek.financeapi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayBillDTO {
	
	private Long billId;
    private Long userId;
	
}
