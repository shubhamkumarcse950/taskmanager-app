package com.taskManagement.Dtos;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesDepartmentEmployeeDto {

	private Long empId;
	private String userCode;
	private String fullName;
	@Column(name = "email", unique = true)
	private String email;
	private String mobileNumber;
	private String address;
	private String designation;
	private String role;
}
