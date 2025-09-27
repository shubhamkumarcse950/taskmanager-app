package com.taskManagement.Dtos;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class DealDTO {
	private Long id;
	private String name;
	private String value;
	private String status;
	private String clientName;
	private MultipartFile proposalUpload;
	private String salesPersonName;
	private String userCode;
	private String isApprovedByAdmin;
	private LocalDate dealDate;
	private String advanceAmount;
	private String payMode;
	private String requirement;
	private String email;
	private String phone;
	private String clientUserCode;
}
