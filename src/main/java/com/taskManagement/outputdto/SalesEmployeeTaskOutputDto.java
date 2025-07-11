package com.taskManagement.outputdto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.taskManagement.Dtos.SalesDepartmentEmployeeDto;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesEmployeeTaskOutputDto {
	private Long seTaskId;
	private LocalDate taskDate;
	private LocalDate endDate;
	private String taskTitle;
	private String task;
	private String userCode;
	private SalesDepartmentEmployeeDto salesDepartmentEmployee;
	private String status;
	@ElementCollection
	private List<String> dailyTask = new ArrayList<>();
}
