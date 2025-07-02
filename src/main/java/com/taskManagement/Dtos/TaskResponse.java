package com.taskManagement.Dtos;

import com.taskManagement.Entitys.Task;
import com.taskManagement.Entitys.TaskProgress;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskResponse {

	private Task task;
	private TaskProgress taskProgress;

	public TaskResponse(Task task, TaskProgress taskProgress) {
		super();
		this.task = task;
		this.taskProgress = taskProgress;
	}

}
