package com.abishek.financeapi.Controller;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abishek.financeapi.DTO.PdfDTO;
import com.abishek.financeapi.Service.PDF.PdfExportService;
import com.abishek.financeapi.Service.Transaction.TransactionService;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin
public class PdfExportController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private PdfExportService pdfExportService;
    
    @GetMapping("/monthly-report")
    public ResponseEntity<byte[]> exportMonthlyTransactionReport() {
        PdfDTO pdfDTO = transactionService.getTransactionDataMonthly();

        ByteArrayInputStream bis = pdfExportService.generateMonthlyTransactionReport(pdfDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=monthly-transaction-report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bis.readAllBytes());
    }
    
//    @GetMapping("/transactions")
//    public ResponseEntity<byte[]> getTransactionsPdf(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//                                                     @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//        byte[] pdfData = pdfExportService.generatePdfForTransactions(startDate, endDate);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDispositionFormData("attachment", "transactions.pdf");
//
//        return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
//    }
}
