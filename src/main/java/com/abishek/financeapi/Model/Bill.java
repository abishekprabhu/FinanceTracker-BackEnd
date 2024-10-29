package com.abishek.financeapi.Model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@Table(name = "bills")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Bill {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonBackReference
	private User user;
	
    @ManyToOne
    @JoinColumn(name = "wallet_id")
    @JsonIgnore
    private Wallet wallet;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;
    
//    @OneToMany(mappedBy = "bills", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private List<TransactionDetails> transactions = new ArrayList<>();
    
	private String description;	
    private double amountDue;
    private LocalDate dueDate;
    private boolean isPaid;
	
}
