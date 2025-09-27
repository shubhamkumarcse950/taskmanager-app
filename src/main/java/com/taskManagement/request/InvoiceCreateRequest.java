package com.taskManagement.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.taskManagement.Dtos.InvoiceLineItemDTO;
import com.taskManagement.enume.InvoiceType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceCreateRequest {
	@NotNull(message = "Invoice type is required")
	private InvoiceType invoiceType;

	@NotNull(message = "Issue date is required")
	private LocalDate issueDate;

	private LocalDate dueDate;
	private LocalDate deliveryDate;
	private BigDecimal overallPrice;

	// Seller
	private String sellerName;
	private String sellerAddress;
	private String companyState;
	private String bankDetails;
	// Buyer
	@NotBlank(message = "Buyer name is required")
	private String buyerName;
	private String buyerAddress;
	private String buyerGst;
	private String buyerPhone;
	private String buyerEmail;
	private String buyerState;
	// Tax
	private String taxLabel = "GST (18%)";
	private BigDecimal taxRate = new BigDecimal("0.18");

	private String notes;
	private String userCode;
	private Long dealId;

	@Valid
	@NotEmpty(message = "At least one line item is required")
	private List<InvoiceLineItemDTO> lineItems;
}