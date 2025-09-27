package com.taskManagement.Entitys;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sales_deals")
@Data
@NoArgsConstructor
public class DealEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	private String name;

	private String value;

	private String status;

	private String clientName;

	private byte[] proposalUpload;
	private String email;
	private String phone;
	private String salesPersonName;
	private String userCode;
	private String isApprovedByAdmin;
	private LocalDate dealDate;
	private String advanceAmount;
	private String payMode;
	private String requirement;
	private String clientUserCode;
}