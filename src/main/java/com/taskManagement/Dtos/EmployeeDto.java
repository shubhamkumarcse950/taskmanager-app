package com.taskManagement.Dtos;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EmployeeDto {

	private Long employeeId;

	private String fullName;

	private String email;

	private String mobileNumber;

	private String role;
	private String address;
	private String designation;
	private Long departmentId;

	private LocalDate joiningDate;
	private String userCode;
	private String password;
}