package com.abishek.financeapi.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abishek.financeapi.Model.TransactionDetails;

@Repository
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Long> {

	List<TransactionDetails> findByWalletId(Long walletId);    
}
