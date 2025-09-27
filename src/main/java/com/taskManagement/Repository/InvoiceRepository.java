package com.taskManagement.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.Invoice;
import com.taskManagement.enume.InvoiceType;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	Optional<Invoice> findTopByInvoiceTypeOrderByIdDesc(InvoiceType invoiceType);

	List<Invoice> findByUserCodeOrderByCreatedAtDesc(String userCode);

	List<Invoice> findByDealId(Long dealId);

	List<Invoice> findByBuyerEmail(String buyerEmail);

	Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

	@Query("SELECT i FROM Invoice i WHERE i.userCode = :userCode AND "
			+ "(:invoiceType IS NULL OR i.invoiceType = :invoiceType) AND "
			+ "(:startDate IS NULL OR i.issueDate >= :startDate) AND "
			+ "(:endDate IS NULL OR i.issueDate <= :endDate)")
	List<Invoice> findInvoicesWithFilters(@Param("userCode") String userCode,
			@Param("invoiceType") InvoiceType invoiceType, @Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);
}