package com.taskManagement.Entitys;

import java.time.LocalDate;
import java.util.Map;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class LeadAssign {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long assignId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_id", nullable = false, referencedColumnName = "empId")
	private SalesDepartmentEmployee salesDepartmentEmployee;
	@ElementCollection
	private Map<Long, String> leads;
	private LocalDate assignDate;
	private String userCode;
//	@ElementCollection
//	private List<String> followUp;

}
