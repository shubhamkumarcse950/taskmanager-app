package com.taskManagement.Dtos;

import java.time.LocalDate;

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
public class TaskDto {

	private Long taskId;
	@Size(min = 1, max = 254, message = "title should not be grater then 254 characters and not less then 10 characters!!")
	private String title;
	@NotNull(message = "description should not be null!!")
	@Size(min = 10, max = 254, message = "description should not be grater then 254 characters and not less then 10 characters!!")
	private String description;
	private String status;
	private String priority;
	private LocalDate startDate;
	private LocalDate endDate;
	private Long developerId;
	private Long projectId;
	private String developerName;
	private String projectName;
}
