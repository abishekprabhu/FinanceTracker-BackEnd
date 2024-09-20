package com.abishek.financeapi.Service.PDF;

import java.io.ByteArrayInputStream;

import com.abishek.financeapi.DTO.PdfDTO;

public interface PdfExportService {

	ByteArrayInputStream generateMonthlyTransactionReport(PdfDTO pdfDTO);

	ByteArrayInputStream generateYearlyTransactionReport(PdfDTO pdfDTO);

	ByteArrayInputStream generateQuartarlyTransactionReport(PdfDTO pdfDTO);

	ByteArrayInputStream generateWeeklyTransactionReport(PdfDTO pdfDTO);

}
