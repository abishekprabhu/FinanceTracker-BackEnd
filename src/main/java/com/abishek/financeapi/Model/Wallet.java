package com.abishek.financeapi.Model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "wallets")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore  // Use JsonIgnore to avoid circular reference
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    private double balance;   
    
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Bill> bills;
    
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
//    @JsonManagedReference
    @JsonIgnore
    private List<TransactionDetails> transactions = new ArrayList<>();
    
    public Wallet() {
        this.balance = 0.0;
    }
}
