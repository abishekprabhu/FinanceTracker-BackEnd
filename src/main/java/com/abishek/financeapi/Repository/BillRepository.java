package com.abishek.financeapi.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abishek.financeapi.Model.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long>{
    List<Bill> findByUserId(Long userId);
    List<Bill> findByWalletId(Long walletId);
    List<Bill> findByIsPaidFalse(); // Find unpaid bills
//	List<Bill> findByDueDateBeforeAndPaidFalse(LocalDate today);
    Optional<Bill> findByIdAndUserId(Long billId, Long userId);
}
