package com.taskManagement.Entitys;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SalesDepartmentEmployee {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long empId;
	private String userCode;
	private String fullName;
	@Column(name = "email", unique = true)
	private String email;
	private String mobileNumber;
	private String address;
	private String designation;
	private String role;
	@OneToMany(mappedBy = "salesDepartmentEmployee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SalesEmployeeTask> salesEmployeeTask = new ArrayList<>();
	@OneToMany(mappedBy = "salesDepartmentEmployee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LeadAssign> leadAssigns = new ArrayList<>();
	@ManyToOne
	@JoinColumn(name = "department_id")
	private Department department;
}
