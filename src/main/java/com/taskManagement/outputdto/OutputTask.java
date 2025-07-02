package com.taskManagement.outputdto;

import java.time.LocalDate;
import java.util.List;

import com.taskManagement.Entitys.TaskProgress;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OutputTask {

	private Long taskId;
	private String title;
	private String description;
	private String status;
	private String priority;
	private LocalDate startDate;
	private LocalDate endDate;
	private LocalDate dueDate;
	private Long developerId;
	private Long projectId;
	private List<TaskProgress> taskProgresses;
}
