package com.taskManagement.outputdto;

import java.time.LocalDate;
import java.util.List;

import com.taskManagement.Dtos.SalesDepartmentEmployeeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LeadAssignOutPutDto {

	private Long assignId;
	private SalesDepartmentEmployeeDto salesDepartmentEmployeeDto;
	private List<Object> leads;
	private LocalDate assignDate;
	private String userCode;
//	private List<String> followUp;
}
