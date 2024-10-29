package com.abishek.financeapi.Model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@Entity
public class TransactionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentMethod;
    private String paymentId;
    private double amount;
    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
//    @JsonBackReference
    @JsonIgnore  
    private Wallet wallet;
    
//    @ManyToOne
//    @JoinColumn(name = "bill_id")
//    @JsonBackReference
//    private Bill bill;
    
    private String receipt; //bill_id
    
    public TransactionDetails() {
        this.transactionDate = LocalDateTime.now();
    }
}
