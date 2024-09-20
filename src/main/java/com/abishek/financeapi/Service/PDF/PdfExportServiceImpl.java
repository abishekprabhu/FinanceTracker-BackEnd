package com.abishek.financeapi.Service.PDF;

import com.abishek.financeapi.DTO.PdfDTO;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class PdfExportServiceImpl implements PdfExportService{
	
		@Override
	    public ByteArrayInputStream generateMonthlyTransactionReport(PdfDTO pdfDTO) {
			LocalDate endDate = LocalDate.now();
			
			LocalDate startDate = endDate.minusDays(30);
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	
	        try {
	            PdfWriter writer = new PdfWriter(out);
	            PdfDocument pdf = new PdfDocument(writer);
	            Document document = new Document(pdf);
	
	//            document.add(new Paragraph("Monthly Transaction Report").setFontSize(18));
	            
	            // Add header
	            addHeader(document, startDate, endDate);
	            
	            // Create Table
	            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 4, 4, 4, 3}))
	                    .useAllAvailableWidth();
	            table.addHeaderCell("ID");
	            table.addHeaderCell("Description");
	            table.addHeaderCell("Amount");
	            table.addHeaderCell("Type");
	            table.addHeaderCell("Date");
	
	            // Fill table with transaction data
	            pdfDTO.getTransactionList().forEach(transaction -> {
	                table.addCell(transaction.getId().toString());
	                table.addCell(transaction.getDescription());
	                table.addCell(String.valueOf(transaction.getAmount()));
	                table.addCell(transaction.getType().toString());
	                table.addCell(transaction.getDate().toString());
	            });
	
	            document.add(table);
	            // Add footer
	            addFooter(document);
	            document.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	
	        return new ByteArrayInputStream(out.toByteArray());
	    }
		
		@Override
	    public ByteArrayInputStream generateYearlyTransactionReport(PdfDTO pdfDTO) {
			LocalDate endDate = LocalDate.now();
			
			LocalDate startDate = endDate.minusDays(365);
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	
	        try {
	            PdfWriter writer = new PdfWriter(out);
	            PdfDocument pdf = new PdfDocument(writer);
	            Document document = new Document(pdf);
	            
	            // Add header
	            addHeader(document, startDate, endDate);
	            
	            // Create Table
	            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 4, 4, 4, 3}))
	                    .useAllAvailableWidth();
	            table.addHeaderCell("ID");
	            table.addHeaderCell("Description");
	            table.addHeaderCell("Amount");
	            table.addHeaderCell("Type");
	            table.addHeaderCell("Date");
	
	            // Fill table with transaction data
	            pdfDTO.getTransactionList().forEach(transaction -> {
	                table.addCell(transaction.getId().toString());
	                table.addCell(transaction.getDescription());
	                table.addCell(String.valueOf(transaction.getAmount()));
	                table.addCell(transaction.getType().toString());
	                table.addCell(transaction.getDate().toString());
	            });
	
	            document.add(table);
	            // Add footer
	            addFooter(document);
	            document.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	
	        return new ByteArrayInputStream(out.toByteArray());
	    }
		
		@Override
	    public ByteArrayInputStream generateQuartarlyTransactionReport(PdfDTO pdfDTO) {
			LocalDate endDate = LocalDate.now();
			
			LocalDate startDate = endDate.minusDays(183);
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	
	        try {
	            PdfWriter writer = new PdfWriter(out);
	            PdfDocument pdf = new PdfDocument(writer);
	            Document document = new Document(pdf);
	            
	            // Add header
	            addHeader(document, startDate, endDate);
	            
	            // Create Table
	            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 4, 4, 4, 3}))
	                    .useAllAvailableWidth();
	            table.addHeaderCell("ID");
	            table.addHeaderCell("Description");
	            table.addHeaderCell("Amount");
	            table.addHeaderCell("Type");
	            table.addHeaderCell("Date");
	
	            // Fill table with transaction data
	            pdfDTO.getTransactionList().forEach(transaction -> {
	                table.addCell(transaction.getId().toString());
	                table.addCell(transaction.getDescription());
	                table.addCell(String.valueOf(transaction.getAmount()));
	                table.addCell(transaction.getType().toString());
	                table.addCell(transaction.getDate().toString());
	            });
	
	            document.add(table);
	            // Add footer
	            addFooter(document);
	            document.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	
	        return new ByteArrayInputStream(out.toByteArray());
	    }
		
		@Override
	    public ByteArrayInputStream generateWeeklyTransactionReport(PdfDTO pdfDTO) {
			LocalDate endDate = LocalDate.now();
			
			LocalDate startDate = endDate.minusDays(183);
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	
	        try {
	            PdfWriter writer = new PdfWriter(out);
	            PdfDocument pdf = new PdfDocument(writer);
	            Document document = new Document(pdf);
	            
	            // Add header
	            addHeader(document, startDate, endDate);
	            
	            // Create Table
	            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 4, 4, 4, 3}))
	                    .useAllAvailableWidth();
	            table.addHeaderCell("ID");
	            table.addHeaderCell("Description");
	            table.addHeaderCell("Amount");
	            table.addHeaderCell("Type");
	            table.addHeaderCell("Date");
	
	            // Fill table with transaction data
	            pdfDTO.getTransactionList().forEach(transaction -> {
	                table.addCell(transaction.getId().toString());
	                table.addCell(transaction.getDescription());
	                table.addCell(String.valueOf(transaction.getAmount()));
	                table.addCell(transaction.getType().toString());
	                table.addCell(transaction.getDate().toString());
	            });
	
	            document.add(table);
	            // Add footer
	            addFooter(document);
	            document.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	
	        return new ByteArrayInputStream(out.toByteArray());
	    }
	
	
	   
	
	
	
	    private void addHeader(Document document, LocalDate startDate, LocalDate endDate) {
	        // Add title
	        Paragraph title = new Paragraph("Personal Finance Tracker").setFontSize(18).setBold();
	        title.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
	        document.add(title);
	
	        // Add subtitle with date range
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
	        String dateRange = "Statement for: " + startDate.format(formatter)
	        							+ " to " + endDate.format(formatter);
	        Paragraph dateRangeParagraph = new Paragraph(dateRange).setFontSize(12);
	        dateRangeParagraph.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
	        document.add(dateRangeParagraph);
	
	        // Add some space
	        document.add(new Paragraph("\n"));
	    }
	    
	    private void addFooter(Document document) {
	        // Add a footer section at the bottom
	        Paragraph footer = new Paragraph("This is a system-generated statement. For more details,"
	        		+ " contact support at support@personalfinancetracker.com.")
	                .setFontSize(10).setItalic();
	        footer.setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
	        footer.setMarginTop(20);
	        document.add(footer);
	    }
    
    
//  @Override
//  public ByteArrayInputStream generateMonthlyTransactionReport2(PdfDTO pdfDTO) {
//		LocalDate endDate = LocalDate.now();
//		
//		LocalDate startDate = endDate.minusDays(30);
//	
//
//      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//
//      try {
//          PdfWriter writer = new PdfWriter(byteArrayOutputStream);
//          PdfDocument pdfDocument = new PdfDocument(writer);
//          Document document = new Document(pdfDocument);
//
//          // Add header
//          addHeader(document, startDate, endDate);
//
//          // Add table with transaction data
//          addTransactionTable(document, pdfDTO);
//
//          // Add footer
//          addFooter(document);
//
//          document.close();
//      } catch (Exception e) {
//          e.printStackTrace();
//      }
//
//      return new ByteArrayInputStream(out.toByteArray());
//  }
//  
//  @Override
//  public byte[] generatePdfForTransactions(LocalDate startDate, LocalDate endDate) {
//		PdfDTO pdfDTO = new PdfDTO();
//		pdfDTO.setTransactionList(transactionRepository.findByDateBetweenOrderByDateDesc(startDate, endDate));	
//
//      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//
//      try {
//          PdfWriter writer = new PdfWriter(byteArrayOutputStream);
//          PdfDocument pdfDocument = new PdfDocument(writer);
//          Document document = new Document(pdfDocument);
//
//          // Add header
//          addHeader(document, startDate, endDate);
//
//          // Add table with transaction data
//          addTransactionTable(document, pdfDTO);
//
//          // Add footer
//          addFooter(document);
//
//          document.close();
//      } catch (Exception e) {
//          e.printStackTrace();
//      }
//      
//      
//
////      System.out.println("Transactions fetched: " + pdfDTO.size()); // Add this to check if data exists
//
//
//      return byteArrayOutputStream.toByteArray();
//  }

//    private void addTransactionTable(Document document, PdfDTO pdfDTO) {
//        // Create a table with 5 columns
//        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 4, 4, 4, 3}))
//                .useAllAvailableWidth();
//
//        // Add table headers
//        table.addHeaderCell("Date");
//        table.addHeaderCell("Description");
//        table.addHeaderCell("Type");
//        table.addHeaderCell("Amount");
//        table.addHeaderCell("Balance");
//
//        // Add transaction rows
//    double balance = 0.0; // Assuming you want to show balance
//
//    for (Transaction transaction : transactions) {
//        System.out.println("Adding transaction to table: " + transaction.getDescription()); // Log each transaction
//        balance += (transaction.getType() == TransactionType.INCOME) ? transaction.getAmount() : -transaction.getAmount();
//        table.addCell(transaction.getDate().toString());
//        table.addCell(transaction.getDescription());
//        table.addCell(transaction.getType().toString());
//        table.addCell(String.format("$%.2f", transaction.getAmount()));
//        table.addCell(String.format("$%.2f", balance));
//    }
//        
//
//        log.info("table :"+ pdfDTO);
//        // Add table to document
//        document.add(table);
//    }


}
