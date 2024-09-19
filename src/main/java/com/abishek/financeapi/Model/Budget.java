package com.abishek.financeapi.Model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "budgets")
public class Budget {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user; // Each budget is linked to a user

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "category_id", nullable = true) 
    private Category category; // Optional: link budget to a specific category

    @Column(nullable = false)
    private BigDecimal amount; // The budget amount

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate; // Budget start date (monthly or weekly)

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate; // Budget end date (monthly or weekly)

    @Column(name = "total_spent", nullable = false)
    private BigDecimal totalSpent = BigDecimal.ZERO; // Tracks total spent within the budget period

    @Column(name = "is_exceeded", nullable = false)
    private boolean isExceeded = false; // Flag to track if budget exceeded

}

