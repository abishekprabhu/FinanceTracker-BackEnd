package com.abishek.financeapi.Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private List<Expense> expenses;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private List<Income> incomes;
    
    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private List<Wallet> wallets;
    
    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private List<Bill> bills;

    @ManyToOne
    @JsonBackReference
    private User user;
}
