package com.abishek.financeapi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abishek.financeapi.Model.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long>{
	Wallet findByUserId(Long userId);		
}
