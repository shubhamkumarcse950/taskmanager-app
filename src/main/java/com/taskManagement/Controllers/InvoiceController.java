package com.taskManagement.Controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.taskManagement.Dtos.InvoiceDTO;
import com.taskManagement.Service.InvoiceService;
import com.taskManagement.enume.InvoiceType;
import com.taskManagement.request.InvoiceCreateRequest;
import com.taskManagement.responsemodel.ApiResponse;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*")
@Slf4j
public class InvoiceController {

	@Autowired
	private InvoiceService invoiceService;

	@PostMapping
	public ResponseEntity<ApiResponse<InvoiceDTO>> createInvoice(@Valid @RequestBody InvoiceCreateRequest request) {
		try {
			InvoiceDTO invoice = invoiceService.createInvoice(request);
			return ResponseEntity.ok(ApiResponse.success("Invoice created successfully", invoice));
		} catch (Exception e) {
			log.error("Error creating invoice: ", e);
			return ResponseEntity.badRequest().body(ApiResponse.error("Failed to create invoice: " + e.getMessage()));
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<InvoiceDTO>> getInvoiceById(@PathVariable Long id) {
		try {
			InvoiceDTO invoice = invoiceService.getInvoiceById(id);
			return ResponseEntity.ok(ApiResponse.success("Invoice retrieved successfully", invoice));
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/number/{invoiceNumber}")
	public ResponseEntity<ApiResponse<InvoiceDTO>> getInvoiceByNumber(@PathVariable String invoiceNumber) {
		try {
			InvoiceDTO invoice = invoiceService.getInvoiceByNumber(invoiceNumber);
			return ResponseEntity.ok(ApiResponse.success("Invoice retrieved successfully", invoice));
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/user/{userCode}")
	public ResponseEntity<ApiResponse<List<InvoiceDTO>>> getInvoicesByUserCode(@PathVariable String userCode) {
		try {
			List<InvoiceDTO> invoices = invoiceService.getAllInvoicesByUserCode(userCode);
			return ResponseEntity.ok(ApiResponse.success("Invoices retrieved successfully", invoices));
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body(ApiResponse.error("Failed to retrieve invoices: " + e.getMessage()));
		}
	}

	@GetMapping("/deal/{dealId}")
	public ResponseEntity<ApiResponse<List<InvoiceDTO>>> getInvoicesByDealId(@PathVariable Long dealId) {
		try {
			List<InvoiceDTO> invoices = invoiceService.getInvoicesByDealId(dealId);
			return ResponseEntity.ok(ApiResponse.success("Invoices retrieved successfully", invoices));
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body(ApiResponse.error("Failed to retrieve invoices: " + e.getMessage()));
		}
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<List<InvoiceDTO>>> searchInvoices(@RequestParam String userCode,
			@RequestParam(required = false) InvoiceType invoiceType,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		try {
			List<InvoiceDTO> invoices = invoiceService.getInvoicesWithFilters(userCode, invoiceType, startDate,
					endDate);
			return ResponseEntity.ok(ApiResponse.success("Invoices retrieved successfully", invoices));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponse.error("Failed to search invoices: " + e.getMessage()));
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<InvoiceDTO>> updateInvoice(@PathVariable Long id,
			@Valid @RequestBody InvoiceCreateRequest request) {
		try {
			InvoiceDTO invoice = invoiceService.updateInvoice(id, request);
			return ResponseEntity.ok(ApiResponse.success("Invoice updated successfully", invoice));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponse.error("Failed to update invoice: " + e.getMessage()));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteInvoice(@PathVariable Long id) {
		try {
			invoiceService.deleteInvoice(id);
			return ResponseEntity.ok(ApiResponse.success("Invoice deleted successfully", null));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(ApiResponse.error("Failed to delete invoice: " + e.getMessage()));
		}
	}

	@GetMapping("/{id}/pdf")
	public ResponseEntity<byte[]> downloadInvoicePDF(@PathVariable Long id) {
		try {
			byte[] pdfBytes = invoiceService.generateInvoicePDF(id);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDisposition(ContentDisposition.attachment().filename("invoice_" + id + ".pdf").build());

			return ResponseEntity.ok().headers(headers).body(pdfBytes);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/invoces-byclient-email")
	public ResponseEntity<List<InvoiceDTO>> getAllInvoicesByClientEmail(@RequestParam String email) {
		List<InvoiceDTO> list = invoiceService.getAllInvoicesByClientEmail(email);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
}