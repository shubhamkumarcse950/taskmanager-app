package com.taskManagement.Dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.taskManagement.enume.InvoiceType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
	private Long id;
	private String invoiceNumber;
	private InvoiceType invoiceType;
	private LocalDate issueDate;
	private LocalDate dueDate;
	private LocalDate deliveryDate;
	private BigDecimal overallPrice;

	// Seller
	private String sellerName;
	private String sellerAddress;
	private String companyState;
	// Buyer
	private String buyerName;
	private String buyerAddress;
	private String buyerGst;
	private String buyerPhone;
	private String buyerEmail;
	private String buyerState;
	// Tax
	private String taxLabel;
	private BigDecimal taxRate;
	private BigDecimal subtotal;
	private BigDecimal taxAmount;
	private BigDecimal totalAmount;

	private String notes;
	private String userCode;
	private Long dealId;
	private String bankDetails;
	private List<InvoiceLineItemDTO> lineItems = new ArrayList<>();
}