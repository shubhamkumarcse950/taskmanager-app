package com.taskManagement.Entitys;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SalesEmployeeTask {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long seTaskId;
	private LocalDate taskDate;
	private LocalDate endDate;
	private String taskTitle;
	@Column(length = 1000)
	private String task;
	private String userCode;
	@ManyToOne
	@JoinColumn(name = "emp_id", referencedColumnName = "empId")
	private SalesDepartmentEmployee salesDepartmentEmployee;
	private String status;
	@ElementCollection
	private List<String> dailyTask = new ArrayList<>();
}
