package com.taskManagement.ServiceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.taskManagement.Dtos.InvoiceDTO;
import com.taskManagement.Dtos.InvoiceLineItemDTO;
import com.taskManagement.Entitys.Invoice;
import com.taskManagement.Entitys.InvoiceLineItem;
import com.taskManagement.Mappers.LeadManagmentMapper;
import com.taskManagement.Repository.InvoiceRepository;
import com.taskManagement.Service.InvoiceService;
import com.taskManagement.enume.InvoiceType;
import com.taskManagement.request.InvoiceCreateRequest;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

	private final InvoiceRepository invoiceRepository;

	private final LeadManagmentMapper leadManagementMapper;

	public InvoiceServiceImpl(InvoiceRepository invoiceRepository, LeadManagmentMapper leadManagementMapper) {
		super();
		this.invoiceRepository = invoiceRepository;
		this.leadManagementMapper = leadManagementMapper;
	}

	@Override
	public InvoiceDTO createInvoice(InvoiceCreateRequest request) {
		try {
			Invoice invoice = new Invoice();

			// Generate invoice number
			String invoiceNumber = generateInvoiceNumber(request.getInvoiceType());
			invoice.setInvoiceNumber(invoiceNumber);

			// Map basic fields
			invoice.setInvoiceType(request.getInvoiceType());
			invoice.setIssueDate(request.getIssueDate());
			invoice.setDueDate(request.getDueDate());
			invoice.setDeliveryDate(request.getDeliveryDate());
			invoice.setOverallPrice(request.getOverallPrice());

			// Set default seller info or use provided
			if (request.getSellerName() == null || request.getSellerName().isEmpty()) {
				invoice.setSellerName("Kanak Drishti Infotech Pvt. Ltd.");
				invoice.setSellerAddress(
						"A- 88, First Floor, F- 03\nNoida Sector- 4\nGautam Buddha Nagar, Uttar Pradesh, 201301\nIndia\n+91-9911210174\nRam Kumar\nGST- 09AAECK2992E1ZD");
			} else {
				invoice.setSellerName(request.getSellerName());
				invoice.setSellerAddress(request.getSellerAddress());
			}

			// Buyer info
			invoice.setBuyerName(request.getBuyerName());
			invoice.setBuyerAddress(request.getBuyerAddress());
			invoice.setBuyerState(request.getBuyerState());
			invoice.setBuyerGst(request.getBuyerGst());
			invoice.setBuyerPhone(request.getBuyerPhone());
			invoice.setBuyerEmail(request.getBuyerEmail());

			// Set tax based on states
			boolean sameState = request.getCompanyState().equalsIgnoreCase(request.getBuyerState());
			if (sameState) {
				invoice.setTaxLabel("SGST (9%)");
				invoice.setTaxRate(new BigDecimal("0.09"));
			} else {
				invoice.setTaxLabel("SGST (9%) + IGST (9%)");
				invoice.setTaxRate(new BigDecimal("0.18"));
			}

			invoice.setNotes(request.getNotes());
			invoice.setUserCode(request.getUserCode());
			invoice.setDealId(request.getDealId());

			// Calculate totals
			calculateInvoiceTotals(invoice, request.getLineItems());

			// Save invoice first
			invoice = invoiceRepository.save(invoice);

			// Add line items
			for (InvoiceLineItemDTO lineItemDTO : request.getLineItems()) {
				InvoiceLineItem lineItem = new InvoiceLineItem();
				lineItem.setInvoice(invoice);
				lineItem.setType(lineItemDTO.getType());
				lineItem.setDescription(lineItemDTO.getDescription());
				lineItem.setPrice(lineItemDTO.getPrice());
				lineItem.setQuantity(lineItemDTO.getQuantity());
				lineItem.setAmount(lineItemDTO.getPrice().multiply(new BigDecimal(lineItemDTO.getQuantity())));

				invoice.getLineItems().add(lineItem);
			}

			invoice = invoiceRepository.save(invoice);

			return leadManagementMapper.toInvoiceDTO(invoice);

		} catch (Exception e) {
			log.error("Error creating invoice: ", e);
			throw new RuntimeException("Failed to create invoice: " + e.getMessage());
		}
	}

	@Override
	public InvoiceDTO updateInvoice(Long id, InvoiceCreateRequest request) {
		Invoice invoice = invoiceRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));

		// Update fields
		invoice.setIssueDate(request.getIssueDate());
		invoice.setDueDate(request.getDueDate());
		invoice.setDeliveryDate(request.getDeliveryDate());
		invoice.setOverallPrice(request.getOverallPrice());

		invoice.setBuyerName(request.getBuyerName());
		invoice.setBuyerAddress(request.getBuyerAddress());
		invoice.setBuyerState(request.getBuyerState());
		invoice.setBuyerGst(request.getBuyerGst());
		invoice.setBuyerPhone(request.getBuyerPhone());
		invoice.setBuyerEmail(request.getBuyerEmail());

		// Update tax based on states
		boolean sameState = request.getCompanyState().equalsIgnoreCase(request.getBuyerState());
		if (sameState) {
			invoice.setTaxLabel("SGST (9%)");
			invoice.setTaxRate(new BigDecimal("0.09"));
		} else {
			invoice.setTaxLabel("SGST (9%) + IGST (9%)");
			invoice.setTaxRate(new BigDecimal("0.18"));
		}

		invoice.setNotes(request.getNotes());

		// Clear existing line items and add new ones
		invoice.getLineItems().clear();

		for (InvoiceLineItemDTO lineItemDTO : request.getLineItems()) {
			InvoiceLineItem lineItem = new InvoiceLineItem();
			lineItem.setInvoice(invoice);
			lineItem.setType(lineItemDTO.getType());
			lineItem.setDescription(lineItemDTO.getDescription());
			lineItem.setPrice(lineItemDTO.getPrice());
			lineItem.setQuantity(lineItemDTO.getQuantity());
			lineItem.setAmount(lineItemDTO.getPrice().multiply(new BigDecimal(lineItemDTO.getQuantity())));

			invoice.getLineItems().add(lineItem);
		}

		// Recalculate totals
		calculateInvoiceTotals(invoice, request.getLineItems());

		invoice = invoiceRepository.save(invoice);
		return leadManagementMapper.toInvoiceDTO(invoice);
	}

// end here
	private String generateInvoiceNumber(InvoiceType invoiceType) {
		Optional<Invoice> lastInvoice = invoiceRepository.findTopByInvoiceTypeOrderByIdDesc(invoiceType);
		int nextNumber = 1;

		if (lastInvoice.isPresent()) {
			String lastNumber = lastInvoice.get().getInvoiceNumber();
			// Extract number part (assuming format like PI001, TI001)
			String numberPart = lastNumber.substring(2);
			nextNumber = Integer.parseInt(numberPart) + 1;
		}

		return String.format("%s%03d", invoiceType.getPrefix(), nextNumber);
	}

	private void calculateInvoiceTotals(Invoice invoice, List<InvoiceLineItemDTO> lineItems) {
		BigDecimal subtotal = lineItems.stream()
				.map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal taxAmount = subtotal.multiply(invoice.getTaxRate()).setScale(2, RoundingMode.HALF_UP);

		BigDecimal totalAmount = subtotal.add(taxAmount);

		invoice.setSubtotal(subtotal);
		invoice.setTaxAmount(taxAmount);
		invoice.setTotalAmount(totalAmount);
	}

	@Override
	public InvoiceDTO getInvoiceById(Long id) {
		Invoice invoice = invoiceRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
		return leadManagementMapper.toInvoiceDTO(invoice);
	}

	@Override
	public InvoiceDTO getInvoiceByNumber(String invoiceNumber) {
		Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
				.orElseThrow(() -> new RuntimeException("Invoice not found with number: " + invoiceNumber));
		return leadManagementMapper.toInvoiceDTO(invoice);
	}

	@Override
	public List<InvoiceDTO> getAllInvoicesByUserCode(String userCode) {
		List<Invoice> invoices = invoiceRepository.findByUserCodeOrderByCreatedAtDesc(userCode);
		return invoices.stream().map(leadManagementMapper::toInvoiceDTO).collect(Collectors.toList());
	}

	@Override
	public List<InvoiceDTO> getInvoicesByDealId(Long dealId) {
		List<Invoice> invoices = invoiceRepository.findByDealId(dealId);
		return invoices.stream().map(leadManagementMapper::toInvoiceDTO).collect(Collectors.toList());
	}

	@Override
	public List<InvoiceDTO> getInvoicesWithFilters(String userCode, InvoiceType invoiceType, LocalDate startDate,
			LocalDate endDate) {
		List<Invoice> invoices = invoiceRepository.findInvoicesWithFilters(userCode, invoiceType, startDate, endDate);
		return invoices.stream().map(leadManagementMapper::toInvoiceDTO).collect(Collectors.toList());
	}

	@Override
	public void deleteInvoice(Long id) {
		if (!invoiceRepository.existsById(id)) {
			throw new RuntimeException("Invoice not found with id: " + id);
		}
		invoiceRepository.deleteById(id);
	}

	@Override
	public byte[] generateInvoicePDF(Long invoiceId) {
		// PDF generation logic would go here
		// For now, returning empty byte array
		return new byte[0];
	}

	@Override
	public List<InvoiceDTO> getAllInvoicesByClientEmail(String email) {
		List<Invoice> invoices = invoiceRepository.findByBuyerEmail(email);
		return invoices.stream().map(item -> leadManagementMapper.toInvoiceDTO(item)).toList();
	}
}