package com.taskManagement.Service;

import java.time.LocalDate;
import java.util.List;

import com.taskManagement.Dtos.InvoiceDTO;
import com.taskManagement.enume.InvoiceType;
import com.taskManagement.request.InvoiceCreateRequest;

public interface InvoiceService {
	InvoiceDTO createInvoice(InvoiceCreateRequest request);

	InvoiceDTO getInvoiceById(Long id);

	InvoiceDTO getInvoiceByNumber(String invoiceNumber);

	List<InvoiceDTO> getAllInvoicesByUserCode(String userCode);

	List<InvoiceDTO> getInvoicesByDealId(Long dealId);

	InvoiceDTO updateInvoice(Long id, InvoiceCreateRequest request);

	void deleteInvoice(Long id);

	List<InvoiceDTO> getInvoicesWithFilters(String userCode, InvoiceType invoiceType, LocalDate startDate,
			LocalDate endDate);

	byte[] generateInvoicePDF(Long invoiceId);

	List<InvoiceDTO> getAllInvoicesByClientEmail(String email);
}