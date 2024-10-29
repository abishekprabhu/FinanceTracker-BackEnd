package com.abishek.financeapi.Model;

import java.time.LocalDate;

import com.abishek.financeapi.DTO.TransactionDTO;
import com.abishek.financeapi.Enum.TransactionType;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private double amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @ManyToOne
    @JoinColumn(name = "income_id")
    @JsonBackReference
    private Income income;

    @ManyToOne
    @JoinColumn(name = "expense_id")
    @JsonBackReference
    private Expense expense;
    
    @ManyToOne
    @JoinColumn(name = "wallet_id")
    @JsonBackReference
    private Wallet wallet;

}

