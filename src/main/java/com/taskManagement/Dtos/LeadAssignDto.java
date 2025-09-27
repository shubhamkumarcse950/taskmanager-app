package com.taskManagement.Dtos;

import java.time.LocalDate;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LeadAssignDto {
	private Long assignId;
	private Long empId;
	private Map<Long, String> leads;
	private LocalDate assignDate;
	private String userCode;
}
