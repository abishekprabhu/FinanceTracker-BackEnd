package com.abishek.financeapi.DTO;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Data;

@Data
public class MonthlyDataDTO {
    private Map<Integer, BigDecimal> incomeData;
    private Map<Integer, BigDecimal> expenseData;

}
