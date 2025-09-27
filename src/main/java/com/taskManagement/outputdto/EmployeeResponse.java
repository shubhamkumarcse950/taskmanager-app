package com.taskManagement.outputdto;

import java.time.LocalDate;

import com.taskManagement.Dtos.DepartmentDTO;

import lombok.Data;

@Data
public class EmployeeResponse {

	private Long employeeId;

	private String fullName;

	private String email;

	private String mobileNumber;

	private String role;
	private String address;
	private String designation;
	private DepartmentDTO departmentDTO;
	private LocalDate joiningDate;
	private String userCode;
}
