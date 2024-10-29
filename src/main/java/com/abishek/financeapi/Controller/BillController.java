package com.abishek.financeapi.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abishek.financeapi.DTO.BillDTO;
import com.abishek.financeapi.Service.Bill.BillService;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin("*")
public class BillController {

    @Autowired
    private BillService billService;

    @PostMapping("/create")
    public ResponseEntity<BillDTO> createBill(@RequestBody BillDTO billDTO) {
        BillDTO createdBill = billService.createBill(billDTO);
        return ResponseEntity.ok(createdBill);
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<BillDTO>> getAllBills(@PathVariable Long userId) {
        List<BillDTO> bills = billService.getAllBills(userId);
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/unpaid/{userId}")
    public ResponseEntity<List<BillDTO>> getUnpaidBills(@PathVariable Long userId) {
        List<BillDTO> unpaidBills = billService.getUnpaidBills(userId);
        return ResponseEntity.ok(unpaidBills);
    }
    
    @PutMapping("/{billId}")
    public ResponseEntity<BillDTO> updateBill(@PathVariable Long billId, @RequestBody BillDTO billDTO) {
        BillDTO updatedBill = billService.updateBill(billId, billDTO);
        return ResponseEntity.ok(updatedBill);
    }

    // Delete a bill
    @DeleteMapping("/{billId}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long billId) {
        billService.deleteBill(billId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/findByBillId")
    public ResponseEntity<?> getBillByUserId(@RequestParam Long userId , @RequestParam Long billId) {
        BillDTO wallet = billService.findByBillID(billId, userId);
        return ResponseEntity.ok(wallet);
    }
}
