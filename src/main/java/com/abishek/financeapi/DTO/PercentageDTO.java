package com.abishek.financeapi.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PercentageDTO {
    private BigDecimal incomePercentage;
    private BigDecimal expensePercentage;
    private BigDecimal balancePercentage;
}
