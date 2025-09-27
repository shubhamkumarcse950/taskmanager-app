package com.taskManagement.Dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceLineItemDTO {
	private Long id;
	private String type;
	private String description;
	private BigDecimal price;
	private Integer quantity;
	private BigDecimal amount;
}