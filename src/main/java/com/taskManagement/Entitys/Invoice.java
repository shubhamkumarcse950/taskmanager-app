package com.taskManagement.Entitys;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.taskManagement.enume.InvoiceType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "invoice_number", unique = true, nullable = false)
	private String invoiceNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "invoice_type", nullable = false)
	private InvoiceType invoiceType;

	@Column(name = "issue_date")
	private LocalDate issueDate;

	@Column(name = "due_date")
	private LocalDate dueDate;

	@Column(name = "delivery_date")
	private LocalDate deliveryDate;

	@Column(name = "overall_price", precision = 10, scale = 2)
	private BigDecimal overallPrice;

	// Seller Information
	@Column(name = "seller_name")
	private String sellerName;

	@Column(name = "seller_address", columnDefinition = "TEXT")
	private String sellerAddress;
	private String companyState;
	// Buyer Information
	@Column(name = "buyer_name")
	private String buyerName;

	@Column(name = "buyer_address", columnDefinition = "TEXT")
	private String buyerAddress;

	@Column(name = "buyer_gst")
	private String buyerGst;

	@Column(name = "buyer_phone")
	private String buyerPhone;

	@Column(name = "buyer_email")
	private String buyerEmail;

	// Tax Information
	@Column(name = "tax_label")
	private String taxLabel = "GST (18%)";

	@Column(name = "tax_rate", precision = 5, scale = 4)
	private BigDecimal taxRate = new BigDecimal("0.18");

	@Column(name = "subtotal", precision = 10, scale = 2)
	private BigDecimal subtotal;

	@Column(name = "tax_amount", precision = 10, scale = 2)
	private BigDecimal taxAmount;

	@Column(name = "total_amount", precision = 10, scale = 2)
	private BigDecimal totalAmount;

	@Column(name = "notes", columnDefinition = "TEXT")
	private String notes;

	@Column(name = "user_code")
	private String userCode;

	@Column(name = "deal_id")
	private Long dealId;

	@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<InvoiceLineItem> lineItems = new ArrayList<>();

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "buyer_state")
	private String buyerState;

	@Column(name = "bank_details")
	private String bankDetails;
}