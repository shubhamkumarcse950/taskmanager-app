package com.taskManagement.Entitys;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long employeeId;

	private String fullName;

	private String email;

	private String mobileNumber;

	private String role;
	private String address;
	private String designation;

	@ManyToOne
	@JoinColumn(name = "department_id")
	private Department department;

	private LocalDate joiningDate;

	private String userCode;
}