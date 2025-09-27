package com.taskManagement.Dtos;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {

	private Long projectId;
	@NotNull(message = "Project should not be null!!")
	@Size(min = 1, message = "project name should not be null!!")
	private String projectName;
	private LocalDate plannedStartDate;
	private LocalDate plannedEndDate;
	private List<Long> developerId;
	private String status;
//	private List<DeveloperDto> developerDtos;
}
