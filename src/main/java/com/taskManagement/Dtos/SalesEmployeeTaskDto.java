package com.taskManagement.Dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesEmployeeTaskDto {
	private Long seTaskId;
	private LocalDate taskDate;
	private LocalDate endDate;
	private String taskTitle;
	private String task;
	private String userCode;
	private Long empId;
	private String status;

}
